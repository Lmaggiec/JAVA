package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {
	
	// prevent instantiation
	private Trie() { }
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) {
		TrieNode root = new TrieNode(null, null, null);
		Indexes firin = new Indexes(0,(short)0,(short)(allWords[0].length()-1));
		root.firstChild = new TrieNode(firin,null,null);
		for(int i = 1; i < allWords.length; i++) {
			Insert(root, root.firstChild, allWords, i, 0);
		}
		return root;
	}
	
	private static void Insert(TrieNode parent, TrieNode ptr, String[] allWords, int wordindex, int charindex) {
		String newword = allWords[wordindex];
		String ptrword = allWords[ptr.substr.wordIndex];
		boolean comchar = false;
		while(charindex <= ptr.substr.endIndex && charindex < newword.length() && ptrword.charAt(charindex)==newword.charAt(charindex)) {
			comchar = true;
			charindex++;
		}
		
		if(!comchar) {
			if(ptr.sibling!=null) {
				Insert(parent, ptr.sibling, allWords, wordindex, charindex);
			}else {
				Indexes newindex = new Indexes(wordindex, (short)charindex, (short)(newword.length()-1));
				ptr.sibling = new TrieNode(newindex,null,null);
				return;
			}
		}else {
			if(charindex<=ptr.substr.endIndex && ptr.firstChild!=null) {
				Indexes difindex = new Indexes(ptr.substr.wordIndex, (short)ptr.substr.startIndex, (short)(charindex-1));
				ptr.substr.startIndex = (short)(difindex.endIndex+1);
				TrieNode difnode = new TrieNode(difindex, ptr, ptr.sibling);
				if(parent.firstChild==ptr) {
					parent.firstChild = difnode;
				}else {
					TrieNode temptr = parent.firstChild;
					while(temptr!=ptr&&temptr.sibling!=null) {
						temptr = temptr.sibling;
					}
					temptr.sibling = difnode;
				}
				Indexes word_index = new Indexes(wordindex, (short)charindex, (short)(newword.length()-1));
				ptr.sibling = new TrieNode(word_index, null, null);
				return;
			}
			if(ptr.firstChild==null) {
				short temendindex = ptr.substr.endIndex;
				ptr.substr.endIndex = (short)(charindex-1);
				Indexes oldchildindex = new Indexes(ptr.substr.wordIndex, (short)charindex, temendindex);
				Indexes newchildindex = new Indexes(wordindex, (short)charindex, (short)(newword.length()-1));
				TrieNode child1 = new TrieNode(oldchildindex,null,null);
				ptr.firstChild = child1;
				child1.sibling = new TrieNode(newchildindex,null,null);
				return;
			}else {
				Insert(ptr, ptr.firstChild, allWords, wordindex, charindex);
			}
		}
	}
	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root, String[] allWords, String prefix) {
		ArrayList<TrieNode> list = new ArrayList<TrieNode>();
		TrieNode ptr = root.firstChild;
		Trie.completion(ptr,allWords,prefix,list);
		if(list.size()>0) {
			return list;
		}
		return null;
	}
	
	private static void completion(TrieNode ptr,String[] allWords, String prefix, ArrayList<TrieNode> list) {
		if(prefix.length() == 0) {
			Trie.add(ptr, allWords, list, prefix);
			return;
		}
		String currentWord = allWords[ptr.substr.wordIndex].substring(ptr.substr.startIndex, ptr.substr.endIndex + 1);
		if(currentWord.startsWith(prefix) || prefix.startsWith(currentWord)) {
			Trie.add(ptr, allWords, list, prefix);
		}
		else if(ptr.sibling != null){
			Trie.completion(ptr.sibling, allWords, prefix, list);
		}
		
	}
	
	private static void add(TrieNode ptr, String[] allWords, ArrayList<TrieNode> list, 
			String prefix) {
		
		if(ptr.firstChild == null && 
				allWords[ptr.substr.wordIndex].startsWith(prefix)) {
			list.add(ptr);
		}
		
		if(ptr.firstChild != null) {
			Trie.add(ptr.firstChild, allWords, list, prefix);
		}
		
		if(ptr.sibling != null) {
			Trie.add(ptr.sibling, allWords, list, prefix);
		}
		
		if(ptr.firstChild == null && ptr.sibling == null)
			return;
	}
	
	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }
