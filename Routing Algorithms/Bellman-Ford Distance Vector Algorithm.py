import json
from socket import *
from multiprocessing import Process
import time

with open('nodes.json', 'r') as fp:
    nodes = json.load(fp)
    fp.seek(0)
    transition_table = json.load(fp)
    # get the dict in the correct form
    bellman_table = {}
    for i in transition_table:
        bellman_table[i] = {}
        for k in transition_table[i]:
            bellman_table[i][k] = {}
            bellman_table[i][k]['distance'] = transition_table[i][k]
            bellman_table[i][k]['next_hop'] = k
    print(bellman_table)


def main():
    node_dict = {}
    # a dict which key is the node name and the value is its port number
    port_dict = {}
    # a dict which key is the port number and the value is the node name of it
    reverse_dict = {}
    # open json file
    node_port = 12000
    # dispense port number to each node
    for i in nodes:
        count_num = 0
        node_dict[i] = nodes[i]
        port_dict[i] = node_port
        reverse_dict[node_port] = i
        node_port += 1
        count_num += 1
    count_num2 = 0
    p = list()
    # dispense process to each node and start them
    for k in nodes:
        p.append(Process(target=one_process, args=(k, nodes[k], bellman_table[k], reverse_dict, port_dict)))
        p[count_num2].start()
        count_num2 += 1
    # join the process when process ends
    for m in range(count_num2):
        p[m].join()


# node_original is used to determine the neighbors of the node and node_dict is used to get the result
def one_process(node, node_original, node_dict, reverse_dict, port_dict):
    record_file = open(file=node+'.txt', mode='w')
    step = 0
    # get port number of the node
    node_port = port_dict[node]
    node_socket = socket(AF_INET, SOCK_DGRAM)
    # bind the port
    node_socket.bind(('127.0.0.1', node_port))
    # sleep to wait other nodes binds their port
    time.sleep(0.5)
    # use timeout exception to help to determine the condition of nodes
    node_socket.settimeout(0.5)
    # after certain rounds the loop will over
    count_num = 80
    # send message to neighbors
    send_message(node_socket, node_original, node_dict, port_dict)
    # print the prefix into txt file
    prefix = ['Node '+node+'\n', '-----------------------'+'\n', '\n']
    for line in prefix:
        record_file.write(line)
    # when count_num less or equal to 0, the loop ends
    while count_num > 0:
        try:
            # get message from its port
            encode_message, sender_address = node_socket.recvfrom(1024)
            message = encode_message.decode()
            # get sender_node_dict from the receiving message
            sender_node_dict = json.loads(message)
            # get sender node's name from its port num
            sender_node = reverse_dict[sender_address[1]]
            # execute Bellman-Ford Algorithm
            for n in sender_node_dict:
                pointing_node = n
                cost = sender_node_dict[n]['distance']
                # check situation which pointing node does not in receiving node's table
                if pointing_node not in node_dict:
                    # if the sending node point to the receiving node, ignore
                    if pointing_node == node:
                        continue
                    # if the pointing node does not exist in receiving node's table, renew the count num
                    count_num = 80
                    step += 1
                    # update the receiving node's table
                    node_dict[pointing_node] = {}
                    node_dict[pointing_node]['distance'] = cost + node_original[sender_node]
                    node_dict[pointing_node]['next_hop'] = sender_node
                    print(str(node) + ' changes to ' + str(bellman_table[node]))
                    # after updating,
                    send_message(node_socket, node_original, node_dict, port_dict)
                    recording = ['Step '+str(step)+'\n', 'From node '+sender_node+'\n', str(sender_node_dict)
                                 + '\n', 'Current DV:'+'\n', str(node_dict)+'\n', '\n']
                    for l in recording:
                        record_file.write(l)
                else:
                    # check situation which pointing node exist in receiving node's table
                    if cost+node_original[sender_node] < node_dict[pointing_node]['distance']:
                        step += 1
                        # renew the count num when it needs to be updated
                        count_num = 80
                        print()
                        node_dict[pointing_node]['distance'] = cost + node_original[sender_node]
                        node_dict[pointing_node]['next_hop'] = sender_node
                        print(str(node) + ' changes to ' + str(node_dict))
                        send_message(node_socket, node_original, node_dict, port_dict)
                        recording = ['Step '+str(step)+'\n', 'From node '+sender_node+'\n', str(sender_node_dict)
                                     + '\n', 'Current DV:'+'\n', str(node_dict)+'\n', '\n']
                        for l in recording:
                            record_file.write(l)
                    else:
                        # when no updates need to be done, reduce the count number
                        # when the count num equal to 1, print the situation
                        if count_num == 1:
                            step += 1
                            recording = ['Step '+str(step)+'\n', 'From node '+sender_node+'\n', str(sender_node_dict)
                                         + '\n', 'Not Update.\n', '\n']
                            for l in recording:
                                record_file.write(l)
                        count_num = count_num - 1
        # if timeout exception occurs, execute the same procedure as it does not update
        except timeout:
            if count_num == 1:
                step += 1
                recording = ['Step ' + str(step) + '\n', 'From node ' + sender_node + '\n',
                             str(sender_node_dict)
                             + '\n', 'Not Update.\n', '\n']
                for l in recording:
                    record_file.write(l)
            count_num = count_num - 1
    # when loop ends, close node socket and close recording file
    time.sleep(2)
    node_socket.close()
    record_file.close()


# send message method
def send_message(node_socket, node_original, node_dict, port_dict):
    # send the table to the neighbors
    for k in node_original:
        receiver_port = port_dict[k]
        message = json.dumps(node_dict).encode()
        node_socket.sendto(message, ('127.0.0.1', receiver_port))


if __name__ == '__main__':
    main()


