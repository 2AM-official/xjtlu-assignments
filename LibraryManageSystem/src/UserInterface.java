import java.util.*;
//allow users to perform operations
public class UserInterface {
    private static Scanner kb;
	private static Formatter f = new Formatter(System.out);
	
	public static void showMenu(List<Book> list) {
		System.out.println(" ");
		kb = new Scanner(System.in);
		System.out.println("Welcome to the list management system, functions provided include the following:");
		System.out.println("\tAdd - add a new book");
		System.out.println("\tUpdate - update book info");
		System.out.println("\tSearch - enquire about book info");
		System.out.println("\tDelete - delete a book");
		System.out.println("\tDisplay - display book(s) info");
		System.out.println("\tQuit - exit from the current level of interactions");
		
		System.out.println("Enter your command here(Enter 'Quit' at any time to exit from current level):");
		String command = kb.nextLine();
		switch(command.toLowerCase()) { 
		//Operation of adding new books
		case "add":                                                                                                  
			System.out.print("Enter a new book ISBN: " );
			String iSBN = kb.nextLine();
			//use quit method to check whether the input equals to 'quit' or not
			quit(iSBN, list);
			//use checkISBN method to check whether the input ISBN is 10 or 13 digit and did not show in the previous list                                                                                    
			iSBN = checkISBN(iSBN, list);                                                                        
			System.out.println("ISBN: " + iSBN + " Entered.");
			System.out.println(" ");
			
			System.out.print("Enter the title: ");
			String title = kb.nextLine();
			quit(title, list);
			System.out.println("Title: " + title +" Entered.");
			System.out.println(" ");
			
			System.out.print("Enter the author: ");
			String author = kb.nextLine();
			quit(author, list);
			//use iterator to traverse the list to check whether the input title and author are qualified
			Iterator<Book> iter = list.iterator();                                                              
			while(iter.hasNext()) {
				Book b = iter.next();
				//use while loop to determine that the title and author do not equal to other books
				while(title.equals(b.getTitle())&&author.equals(b.getAuthor())) {                            
						System.out.println("Book "+ b.getTitle() +" by "+ b.getAuthor()+" exists in system.");
						System.out.println("Please re-enter Author or enter 'T' to re-enter title: ");
						String textin = kb.nextLine();
						quit(textin, list);
						 //check whether the input is 'T' or not to determine title or author should be re-entered
						if(textin.equals("T")) {                                                                 
							title = kb.nextLine();
							quit(title, list);
							System.out.println("Title has been changed to "+title);
							
						}
						else {
							author = textin;
							quit(author, list);
						}
					}
				}
			System.out.println("Author: " + author + " Entered." );
			System.out.println(" ");
			
			System.out.print("Enter category(Arts, Business, Comics, IT, Cooking, Sports): ");
			String category = kb.nextLine();
			quit(category, list);
            //use checkCategory method to check whether the input category is one of the six category which can be chosen from
			category = checkCategory(category, list);                                                                        
			System.out.println("Category " + category + " Entered.");
			System.out.println(" ");
			
			System.out.print("Enter the total copy number: ");
            //use getIntInput method to get valid input number
			int copyNum = getIntInput(list);      
            //use checkCopyNum method to check whether the input number is less than 20 or not                                                                
			copyNum = checkCopyNum(copyNum, list);                                                                
			int avaliableNum = copyNum;
			System.out.println("Ready to add book: "+iSBN+"; "+title+"; "+author+"; "+category+"; "+copyNum+"; "+avaliableNum);
			System.out.println(" ");
            //determine whether to insert a new book or not by checking the input letter is equal to 'T'
			System.out.println("Enter 'Y' to add new book. Anything else to quit: "); 
            String decision = kb.nextLine();                            
			if(decision.equalsIgnoreCase("Y")) {
                //create a new book
				Book newbook = new Book(iSBN, title, author, category, copyNum);
                //add the new book into the list                              
				list.add(newbook);                                                                            
				System.out.println("New book added successfully.");
			}
			else {
				System.out.println("The operation "+command+" has been cancelled");
			}
			System.out.println(" ");
			System.out.println("Note: automatically exit from current level.");	
		break;
		
		case "delete":
            //check whether the list is empty or not, if the list is empty, can not do delete operation
			if(list.isEmpty()) {     
                                                                                             
				System.out.println("There is no book in your library, please add some books.");
				System.out.println("Note: automatically exit from current level.");
				break;
			}
			System.out.print("Enter the book's ISBN or title + author: ");
			String input = kb.nextLine();
			quit(input, list);
            //check the input is ISBN number or title + author
			if(!(input.contains("+"))) {                                                                           
				String iSBNcheck = input;
                //sse iterator to traverse the list to search
				Iterator<Book> iter2 = list.iterator();                                                        
				while(iter2.hasNext()) {
					Book deletebook = iter2.next();
                    //use get() to compare to the input 
					if(deletebook.getiSBN().equals(iSBNcheck)) {                                           
						System.out.println(" ");
						System.out.println("Found book:");
						outputAll(deletebook);
						System.out.println(" ");
                        //if the total number of books do not equal to available number of books, cannot do the delete operation
						if(!(deletebook.getNumOfBook() == deletebook.getNumAvailable())){             
							int countnum = deletebook.getNumOfBook() - deletebook.getNumAvailable();
                            //output the book which has been lent 
							System.out.println("Sorry this book cannot be deleted. There are "+ countnum +" copies been lent out"); 
							System.out.println(" ");
                                                        System.out.println("Note: automatically exit from current level.");
							showMenu(list);
						}
						else {
							System.out.println("Enter 'Y' to delete the book. Anything else to quit: ");           
							String decision2 = kb.nextLine();
							quit(decision2, list);
                            //check whether to delete the book or not 
							if(decision2.equalsIgnoreCase("Y")) {                                  
								list.remove(deletebook);
								System.out.println("Book with ISBN "+ iSBNcheck + " has been deleted successfully");
								System.out.println("Note: automatically exit from current level.");
								showMenu(list);
							}
							else {
								System.out.println("The operation "+command+" has been cancelled");
								System.out.println("Note: automatically exit from current level.");
								showMenu(list);
							}
						}
				    }
				}	
				System.out.println("Did not search the book your want to delete");
				System.out.println("Note: automatically exit from current level.");
				showMenu(list);
			}
			else {
                //use split to split input from '+' and put into an array
				String str[] = input.split("\\+");                                                              
				String titleCheck = str[0];
				String authorCheck = str[1];		
				Iterator<Book> iter3 = list.iterator();
				while(iter3.hasNext()) {
					Book deletebook = iter3.next();
					if(deletebook.getTitle().equals(titleCheck)) {
                        //if the title and author of a book are both equal to the input 
						if(deletebook.getAuthor().equals(authorCheck)) {                                
							System.out.println("Found book:");
							outputAll(deletebook);
                            //if the number of a book is not equal to the available number of it, it cannot be deleted
							if(!(deletebook.getNumOfBook() == deletebook.getNumAvailable())){       
								int countnum = deletebook.getNumOfBook() - deletebook.getNumAvailable();
								System.out.println("Sorry this book cannot be deleted. There are "+ countnum +" copies been lent out");
							}
							else {
								System.out.println("Enter 'Y' to delete the book. Anything else to quit: ");
								String decision2 = kb.nextLine();
								quit(decision2, list);
                                //check whether the user decide to delete the book or not
								if(decision2.equalsIgnoreCase("Y")) {                            
									list.remove(deletebook);
									System.out.println("\tBook with title: "+titleCheck+" and author: " +authorCheck+ " has been deleted successfully");
								}
								else {
									System.out.println("The operation "+command+" has been cancelled");
								}
							}
                                                        System.out.println(" ");
							System.out.println("Note: automatically exit from current level.");
							showMenu(list);
						}
					}
				}
                                System.out.println(" ");
				System.out.println("Did not search the book your want to delete");
                                System.out.println(" ");
				System.out.println("Note: automatically exit from current level.");
			}
			break;		
			
		case "search":         
            //if the list is empty, tell users cannot operate search                                                        
			if(list.isEmpty()) {                                                                                       
				System.out.println("There is no book in your library, please add some books.");
				System.out.println("Note: automatically exit from current level.");
				break;
			}
			System.out.println("Enter your keyword: ");
			String search = kb.nextLine();
			quit(search, list);
            //use searchOperation to search the list
			searchOperation(search, list);                                                                              
			System.out.println(" ");
			System.out.println("Note: automatically exit from current level.");	
			break;
			
		case "update":
			if(list.isEmpty()) {
				System.out.println("There is no book in your library, please add some books.");
				System.out.println(" ");
				System.out.println("Note: automatically exit from current level.");
				break;
			}
			System.out.println("Enter ISBN: ");
			String updateISBN = kb.nextLine();
			quit(updateISBN, list);
            //use updateOperation to do update 
			updateOperation(updateISBN, list);                                                                           
			System.out.println(" ");
			System.out.println("Note: automatically exit from current level.");
			break;
			
		case "display":
			if(list.isEmpty()) {
				System.out.println("There is no book in your library, please add some books.");
				System.out.println("Note: automatically exit from current level.");
				break;
			}
            //check which order does the user want to show
			System.out.println("Enter 'C' for displaying group by category, or 'A' for displaying group by author:");    
			String checkString = kb.nextLine();
			quit(checkString, list);
			if(checkString.equalsIgnoreCase("C")) {
                //use map to let the display show depends on its category
				Map<String, ArrayList<Book>> map = new HashMap<>();                                                  
				Iterator<Book> iter4 = list.iterator();
				while(iter4.hasNext()) {
					Book b = iter4.next();
					String checkCategory = b.getCategory();
					if(map.containsKey(checkCategory)) {
						map.get(checkCategory).add(b);
					}
					else {
						ArrayList<Book> checklist = new ArrayList<>();
						checklist.add(b);
						map.put(checkCategory, checklist);
					}
				}
                                System.out.println(" ");
				outputFormat();
				for(String e: map.keySet()) {                                                                         
					Iterator<Book> iter6 = map.get(e).iterator();
					while(iter6.hasNext()) {
						outputBook(iter6.next());
					}
				}
			}
			if(checkString.equalsIgnoreCase("A")) {
				Map<String, ArrayList<Book>> map = new HashMap<>();
				Iterator<Book> iter5 = list.iterator();
				while(iter5.hasNext()) {
					Book b = iter5.next();
					String checkAuthor = b.getAuthor();
					if(map.containsKey(checkAuthor)) {
						map.get(checkAuthor).add(b);
					}
					else {
						ArrayList<Book> checklist = new ArrayList<>();
						checklist.add(b);
						map.put(checkAuthor, checklist);
					}
				}
				outputFormat();
				for(String e: map.keySet()) {
					Iterator<Book> iter8 = map.get(e).iterator();
					while(iter8.hasNext()) {
						outputBook(iter8.next());
					}
				}	
			}
                        else{
                            System.out.println(" ");
                            System.out.println("The input can only be 'a' or 'c'");  
                        }
			System.out.println("Note: automatically exit from current level.");	
			break;
		case "quit":
			System.out.println("Exit the program.");	
                        System.exit(0);
			default:
				System.out.println("Please input validated order:");
		}
		if(!(command.toLowerCase().equals("quit"))){
			showMenu(list);
		}
	}
	//method to let users input numbers and check whether numbers are qualified or not
	private static int getIntInput(List<Book> list){                                                                         
        int input = 0;
        String check = kb.nextLine();
        //if the input equals to 'quit' go back to the last level
        if(check.equalsIgnoreCase("quit")) {                                                                                     
        	showMenu(list);
        	
        }
        try{
            input = Integer.parseInt(check);
        }
        catch(NumberFormatException e){
            System.out.println("That is not an int, please try again");
            input = getIntInput(list);
        }
        finally{
            return input;    
        }
    }
	//use suitable format
	public static void outputFormat() {                                                                                       
		f.format("%-15s %-15s %-15s %-10s %-15s %-15s ", "ISBN", "TITLE", "AUTHOR", "CATEGORY", "TOTAL_COPIES", "AVAILABLE_COPIES");
		System.out.println(" ");
	}
	
	public static void outputBook(Book b) {
		f.format("%-15s %-15s %-15s %-10s %-15d %-15d ", b.getiSBN(), b.getTitle(), b.getAuthor(), b.getCategory(), b.getNumOfBook(), b.getNumAvailable());
		System.out.println(" ");
	}
	
	public static void outputAll(Book b) {
		outputFormat();
		outputBook(b);
	}
	
	public static void quit(String quit, List<Book> list) {
		if(quit.equalsIgnoreCase("Quit")) {
			System.out.println("Exit from the current level.");
			showMenu(list);
		}
	}
	
	public static String checkISBN(String iSBN, List<Book> list) {
		Iterator<Book> iter = list.iterator();
		while(iter.hasNext()) {
                    Book checkBook = iter.next();
            //check whether the ISBN is contained in the list before or not
		    if(iSBN.equals(checkBook.getiSBN())) {                                                                  
			System.out.println(" ");
                        System.out.println("This ISBN number has already been recorded in the library.");
                        System.out.println(" ");
			System.out.println("Please re-enter the ISBN of a new book(or print 'Quit' to exit): ");
			iSBN = kb.nextLine();
			quit(iSBN, list);
                        iSBN = checkISBN(iSBN, list);
		    }
		}
        //check wether the ISBN is equal to 10 or 13 digit or not
		if(!(iSBN.length()==10||iSBN.length()==13)) {                                                                       
		    System.out.println(" ");
                    System.out.println("The ISBN number shuold be a 10 or 13-digit number");
                    System.out.println(" ");
		    System.out.println("Please re-enter the ISBN of a new book(or print 'Quit' to exit): ");
		    iSBN = kb.nextLine();
		    quit(iSBN, list);
                    iSBN = checkISBN(iSBN, list);
		}

                char[] str = new char[iSBN.length()];
                    str = iSBN.toCharArray();
                    for(int i = 0; i < str.length; i ++){
                        if(Character.isDigit(str[i])==false){

                            System.out.println(" ");
                            System.out.println("Your input ISBN do not only contain digit");
                            System.out.println(" ");
                            System.out.println("Please re-enter the ISBN of a new book(or print 'Quit' to exit): ");
			    iSBN = kb.nextLine();
			    quit(iSBN, list);     
                            iSBN = checkISBN(iSBN, list);
                        
                    }
                }
		return iSBN;
	}
	//check whether the input category is one of the six qualified categories
	public static String checkCategory(String category, List<Book> list) {                                                        
		if(!(category.toLowerCase().equals("arts")||category.toLowerCase().equals("business")||category.toLowerCase().equals("comics")||category.toLowerCase().equals("it")||
				category.toLowerCase().equals("cooking")||category.toLowerCase().equals("sports"))) {
			System.out.println("Your input does not belong to 'Arts', 'Business', 'Comics', 'IT', 'Cooking', 'Sports'");
			System.out.println("Please re-enter or enter 'Quit' to exit: ");
			category = kb.nextLine();
			quit(category, list);
			category = checkCategory(category, list);
		}
		return category;
	}
	//check whether the number of book is less than 20 or not
	public static int checkCopyNum(int copyNum, List<Book> list) {                                                                 
		if(copyNum<=0||copyNum>20) {
			System.out.println("The number of books should be in 20");
			System.out.println("Please re-enter or enter 'Quit' to exit: ");
			copyNum = getIntInput(list);
			copyNum = checkCopyNum(copyNum, list);
		}
		return copyNum;
	}
	//search books which contains the related information and show them depends on the number of them
	public static void searchOperation(String search, List<Book> list) {                                                             
		Boolean searchBoolean = false;
		Iterator<Book> iter = list.iterator();
		while(iter.hasNext()) {
			Book searchBook = iter.next();
			if(searchBook.getTitle().contains(search)||searchBook.getTitle().contains(search)||searchBook.getAuthor().contains(search)||searchBook.getCategory().contains(search)) {
				searchBoolean = true;
			}
		}
		if(searchBoolean == true) {
			ArrayList<Book> search_book = new ArrayList<>();
                        System.out.println(" ");
			System.out.println("Found book: ");
			outputFormat();
			Iterator<Book> iter2 = list.iterator();
			while(iter2.hasNext()) {
				Book searchBook2 = iter2.next();
				if(searchBook2.getTitle().contains(search)||searchBook2.getTitle().contains(search)||searchBook2.getAuthor().contains(search)||searchBook2.getCategory().contains(search)) {
					search_book.add(searchBook2);
				}
		    }
            //use method of Sort_by_copynum to sort the books depends on the number of them
			SortByCopynum(search_book);                                                                                          
			Iterator<Book> iter3 = search_book.iterator();
			while(iter3.hasNext()) {
				outputBook(iter3.next());
			}
		}
		else {
			System.out.println("Your search item does not contain in the database");
			System.out.println("Please re-enter keywords or enter 'quit' to exit: ");
			search = kb.nextLine();
			quit(search, list);
			searchOperation(search, list);
			}
		}
	//methods of update operation
	public static void updateOperation(String updateISBN, List<Book> list) {                                                                
		Boolean updateBoolean = false;
		Iterator<Book> iter = list.iterator();
		while(iter.hasNext()) {
			Book updatebook = iter.next();
			if(updatebook.getiSBN().equals(updateISBN)) {
				updateBoolean = true;
			}
		}
		if(updateBoolean == true) {
			Iterator<Book> iter2 = list.iterator();
			while(iter2.hasNext()) {
				Book updatebook = iter2.next();
				if(updatebook.getiSBN().equals(updateISBN)) {
					System.out.println("Found book: ");
					outputAll(updatebook);
					System.out.println(" ");
					System.out.println("Enter type of information you want to update, 'T' for title, 'A' for author, 'C' for category, 'TC' for total copy number, 'AC' for available number: ");
					String checkLetter = kb.nextLine();
                    //check whether the input order related to the categories that can be updated
					quit(checkLetter, list);                                                                                  
                                        while((!checkLetter.equalsIgnoreCase("T"))&&(!checkLetter.equalsIgnoreCase("A"))&&(!checkLetter.equalsIgnoreCase("C"))&&(!checkLetter.equalsIgnoreCase("TC"))&&(!checkLetter.equalsIgnoreCase("AC"))){
                                            System.out.println(" ");
                                            System.out.println("Your input does not belong to these categories");
                                            System.out.println(" ");
			                    System.out.println("Please re-enter or enter 'Quit' to exit: ");
                                            checkLetter = kb.nextLine();
                                            quit(checkLetter, list);
                                        }
					if(checkLetter.equalsIgnoreCase("T")) {
						System.out.println("Enter your new title:");
						String updateTitle = kb.nextLine();
						quit(updateTitle, list);
						System.out.println("Book "+updateISBN+"'s title has been updated from "+updatebook.getTitle()+" to "+updateTitle+" successfully.");
						updatebook.setTitle(updateTitle);
					}
					if(checkLetter.equalsIgnoreCase("A")) {
						System.out.println("Enter your new author:");
						String updateAuthor = kb.nextLine();
						quit(updateAuthor, list);
						System.out.println("Book "+updateISBN+"'s author has been updated from "+updatebook.getAuthor()+" to "+updateAuthor+" successfully.");
						updatebook.setAuthor(updateAuthor);
					}
					if(checkLetter.equalsIgnoreCase("C")) {
						System.out.println("Enter your new category:");
						String updateCategory = kb.nextLine();
						quit(updateCategory, list);
						updateCategory = checkCategory(updateCategory, list);
						System.out.println("Book "+updateISBN+"'s category has been updated from "+updatebook.getCategory()+" to "+updateCategory+" successfully.");
						updatebook.setCategory(updateCategory);
					}
					if(checkLetter.equalsIgnoreCase("TC")) {
						System.out.println("Enter your new total_copy_number:");
						int updateTC = getIntInput(list);
						updateTC = checkCopyNum(updateTC, list);
						System.out.println("Book "+updateISBN+"'s total_copy_number has been updated from "+updatebook.getNumOfBook()+" to "+updateTC+" successfully.");
						updatebook.setNumOfBook(updateTC);;
					}
					if(checkLetter.equalsIgnoreCase("AC")) {
						System.out.println("Enter your new avaliable_copy_number:");
						int updateAC = getIntInput(list);
                                                //the number of available books cannot exceed the number of all books
                                                while(updateAC>updatebook.getNumOfBook()){                                                           
                                                    System.out.println(" ");
                                                    System.out.println("The number of avaliable books cannot be bigger than the number of all books");
                                                    System.out.println(" ");
                                                    System.out.println("Please re-enter your update avaliable_copy_number or input 'Quit' to quit");
                                                    updateAC = getIntInput(list);
                                                }
						updateAC = checkCopyNum(updateAC, list);
						System.out.println("Book "+updateISBN+"'s available_copy_number has been updated from "+updatebook.getNumAvailable()+" to "+updateAC+" successfully.");
						updatebook.setNumAvailable(updateAC);;
					}
				}
			}
			
		}
		else {
			System.out.println("Cannot find the book you want to update");
			System.out.println("Please re-enter or enter 'Quit' to exit: ");
			updateISBN = kb.nextLine();
			quit(updateISBN, list);
			updateOperation(updateISBN, list);
		}
	}
	//use comparator to compare the number of books 
	public static ArrayList<Book> SortByCopynum(ArrayList<Book> to_search) {                                                                                                 
        /*
        * sort by the total copy
        * */
        Comparator <Book> comparator = new Comparator<Book>() {
            @Override
            public int compare(Book book1, Book book2) {
                if (book1.getNumOfBook() > book2.getNumOfBook()) {
                    return -1;  
                } else if (book1.getNumOfBook() < book2.getNumOfBook()) {
                    return 1;  
                } else {
                    return 0;  
                }
            }
        };
        to_search.sort(comparator);
        return to_search;
    }
    
}
