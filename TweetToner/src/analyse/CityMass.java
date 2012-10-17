package analyse;

public class CityMass {
int all, tweets, deleted, fsq, insta;
String city;

	public CityMass(String city, int tweets, int deleted, int fsq, int insta){
		this.city = city;
		this.tweets = tweets;
		this.deleted = deleted;
		this.fsq = fsq;
		this.insta = insta;
		all = tweets + deleted + fsq + insta;
	}
}
