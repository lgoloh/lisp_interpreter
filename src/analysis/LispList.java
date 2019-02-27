package analysis;

import java.util.ArrayList;
import java.util.Collection;


public class LispList{
	
	private ArrayList<String> mLispList;
	
	public LispList() {}
	
	public boolean add(String tokenValue) {
		mLispList.add(tokenValue);
		return true;
	}
	
	
	public String toString() {
		String resList = "(";
		for (String obj : mLispList) {
			resList += " " + obj;
		}
		return resList;
	}

	public int size() {
		int count = 0;
		for (int i = 0; i < mLispList.size(); i++) {
			count++;
		}
		return count;
	}

	
	public boolean isEmpty() {
		if (mLispList.size() != 0 ) {
			return false;
		}
		return true;
	}
	
	public boolean remove(String o) {
		mLispList.remove(o);
		return true;
	}


	public boolean addAll(Collection<? extends String> c) {
		mLispList.addAll(c);
		return false;
	}

	
	public boolean addAll(int index, Collection<? extends String> c) {
		mLispList.addAll(index, c);
		return false;
	}


	
	public String get(int index) {
		return mLispList.get(index);
	}

	
	public String set(int index, String element) {
		return mLispList.set(index, element);
	}

	
	public void add(int index, String element) {
		mLispList.add(index, element);
	}
}
