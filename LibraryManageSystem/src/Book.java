public class Book {    
    //book fields                                  
    private String iSBN;
	private String title;
	private String author;
	private String category;
	private int numOfBook;
	private int numAvailable;
	
    //constructor
	public Book(String iSBN, String title, String author, String category, int numOfBook){
		this.iSBN = iSBN;
		this.title = title;
		this.author = author;
		this.category = category;
		this.numOfBook = numOfBook;
		this.numAvailable = numOfBook;
	}

    //getters and setters
	public String getiSBN() {
		return iSBN;
	}

	public void setiSBN(String iSBN) {
		this.iSBN = iSBN;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getNumOfBook() {
		return numOfBook;
	}

	public void setNumOfBook(int numOfBook) {
		this.numOfBook = numOfBook;
	}

	public int getNumAvailable() {
		return numAvailable;
	}

	public void setNumAvailable(int numAvailable) {
		this.numAvailable = numAvailable;
	}
    
}
