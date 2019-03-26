package movieapp;
/*
 * 
 * This class implements a library of movies
 *
 * @author runb-cs111
 *
 */
public class MovieApp {

    private Movie[] items;     // keep movies in an unsorted array
    private int numberOfItems; // number of movies in the array
	
    /*
     * Default constructor allocates array with capacity for 20 movies
     */
    MovieApp () {
	/** COMPLETE THIS METHOD **/
        items = new Movie[20];
    }
	
    /*
     * One argument constructor allocates array with user defined capacity
     *
     * @param capacity defines the capacity of the movie library
     */
    MovieApp (int capacity) {
	/** COMPLETE THIS METHOD **/
        items = new Movie[capacity];
    }

    /*
     * Add a movie into the library (unsorted array)
     *
     * Inserts @m into the first empty spot in the array.
     * If the array is full (there is no space to add another movie)
     *   - create a new array with double the size of the current array
     *   - copy all current movies into new array
     *   - add new movie
     *
     * @param m The movie to be added to the libary
     *
     */
    public void addMovie (Movie m) {
	/** COMPLETE THIS METHOD **/
        if(getNumberOfItems()<items.length){
            items.add(m);
        }
        else{
            Movie[] newitems = new Movie[2*items.length];
            for(int i=0; i<items.length; i++){
                newitems[i]=items[i];
                newitems.add(m);
            }
        }
    }

    /*
     * Removes a movie from the library. Returns true if movie is removed, false
     * otherwise.
     * The array should NOT become sparse after a remove, that is, all movies
     * should be in consecutive array indices.
     * 
     * @param m The movie to be removed
     * @return Returns true is movie is successfuly removed, false otherwise
     *
     */
    public boolean removeMovie (Movie m) {
	/** COMPLETE THIS METHOD **/
	// THE FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
	// Change as needed for your implementation
        for(int i=0; i<items.length;i++){
            if(items[i]==m){
                items.remove(m);
                for(int k=i;k<getNumberOfItems();k++){
                    items[k]=items[k+1];
                }
                items[getNumberOfItems()]=null;
                return true;
            }
        }
	return false;
    }

    /*
     * Returns the library of movies
     *
     * @return The array of movies
     */
    public Movie[] getMovies () {
	/** COMPLETE THIS METHOD **/
	// THE FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
	// Change as needed for your implementation
	return items;
    }

    /*
     * Returns the current number of movies in the library
     *
     * @return The number of items in the array
     */
    public int getNumberOfItems () {
	/** COMPLETE THIS METHOD **/
	// THE FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
	// Changed as needed for your implementation
    int num=0;
    for(int i=0; i<items.length; i++){
        if(items[i]!=null){
            num++;
        }
    }
	return num;
    }
    
    /*
     * Update the rating of movie @m to @ratings
     * 
     * @param @m The movie to have its ratings updated
     * @param @ratings The new ratings of @m
     * @return tue if update is successfull, false otherwise
     *
     */
    public boolean updateRating (Movie m, int ratings) {
	/** COMPLETE THIS METHOD **/
	// THE FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
	// Changed as needed for your implementation	
	m.setRatings(ratings);
        if(m.getRatings()==ratings){
            return true;
        }
        return false;
    }
    
    /*
     * Prints all movies
     */
    public void print () {
	/** COMPLETE THIS METHOD **/
        for(int i=0; i<items.length;i++){
            System.out.println(items[i]);
        }
    }

    /*
     * Return all the movies by @director. The array size should be the 
     * number of movies by @director.
     *
     * @param director The director's name
     * @return An array of all the movies by @director
     *
     */
    public Movie[] getMoviesByDirector (String director) {
	/** COMPLETE THIS METHOD **/
	// THE FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
	// Changed as needed for your implementation
        int count=0;
        int index=0;
        for(int i=0; i<items.length;i++){
            if(items[i].getDirector()==director){
                count++;
            }
        }
        Movie[] SamDic=new Movie[count];
        for(int j=0; j<items.length;j++){
            if(items[j].getDirector()==director){
                SamDic[index]=items[j];
                index++;
            }
        }
        return SamDic;
    }
    
    /*
     * Returns all the movies made in @year. The array size should be the
     * number of movies made in @year.
     *
     * @param year The year the movies were made
     * @return An array of all the movies made in @year
     *
     */
    public Movie[] getMoviesByYear (int year) {
	/** COMPLETE THIS METHOD **/
	// THE FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
	// Changed as needed for your implementation	
        int count=0;
        int index=0;
        for(int i=0; i<items.length;i++){
            if(items[i].getYear()==year){
                count++;
            }
        }
        Movie[] SamYea=new Movie[count];
        for(int j=0; j<items.length;j++){
            if(items[j].getYear()==year){
                SamYea[index]=items[j];
                index++;
            }
        }
        return SamYea;
    }
	
    /*
     * Returns all the movies with ratings greater then @ratings. The array
     * size should match the number of movies with ratings greater than @ratings
     *
     * @param ratings
     * @return An array of all movies with ratings greater than @ratings
     *
     */
    public Movie[] getMoviesWithRatingsGreaterThan (int ratings) {
	/** COMPLETE THIS METHOD **/
	// THE FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
	// Changed as needed for your implementation	
	int count=0;
    int index=0;
        for(int i=0; i<items.length;i++){
            if(items[i].getRatings() > ratings){
                count++;
            }
        }
        Movie[] Greater=new Movie[count];
        for(int j=0; j<items.length;j++){
            if(items[j].getRatings() > ratings){
                Greater[index]=items[j];
                index++;
            }
        }
        return Greater;
    }
}
