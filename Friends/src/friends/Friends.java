package friends;

import structures.Queue;
import structures.Stack;

import java.util.*;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		
		boolean[] visited = new boolean[g.members.length];
		int[] prevV = new int[g.members.length];
		ArrayList<String> result = new ArrayList<>();
		Person start = g.members[g.map.get(p1)];
		Person end = g.members[g.map.get(p2)];
		boolean found = false;
		Queue<Person> Q = new Queue<>();
		Q.enqueue(start);

		for(int i = 0; i < prevV.length; i++) {
			prevV[i] = -1;
		}
		while(!Q.isEmpty()) {
			Person current = Q.dequeue();
			if(current == end) {
				found = true;
				break;
			}
			
			int num = g.map.get(current.name);
			visited[num] = true;
			
			Friend friendPtr = g.members[num].first;
			while(friendPtr != null) {
				if(!visited[friendPtr.fnum]) {
					Q.enqueue(g.members[friendPtr.fnum]);
					prevV[friendPtr.fnum] = num;
					visited[friendPtr.fnum] = true;
				}
				friendPtr = friendPtr.next;
			}
		}
		
		if(!found)
			return null;
		
		Stack<String> s = new Stack<>();
		s.push(end.name);
		int targetNum = g.map.get(end.name);
		int prevNum = prevV[targetNum];
		while(prevNum != -1) {
			s.push(g.members[prevNum].name);
			prevNum = prevV[prevNum];
		}
		while(!s.isEmpty()) {
			result.add(s.pop());
		}
		return result;
	}
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		
		ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
		boolean[] visited = new boolean[g.members.length];
		
		for(int i = 0; i < g.members.length; i++) {
			ArrayList<String> temp = new ArrayList<String>();
			Person currP = g.members[i];
			int currNum = g.map.get(currP.name);
			
			if(currP.school == null)
				continue;
		
			if(currP.school.equals(school) && visited[currNum] == false) {
				BFS(g, school, currP, visited, temp);
				result.add(temp);
			}
		}
		return result;		
	}
	
	private static void BFS(Graph g, String school, Person p, boolean[] visited, ArrayList<String> a) {
		
		Queue<Person> Q = new Queue<Person>();
		Q.enqueue(p);
		
		while(!Q.isEmpty()) {
			Person currP = Q.dequeue();
			a.add(currP.name);
			int currNum = g.map.get(currP.name);
			visited[currNum] = true;
			Friend friendPtr = g.members[currNum].first;
			while(friendPtr!=null) {
				String friendsch = g.members[friendPtr.fnum].school;
				if(friendsch==null) {
					friendPtr = friendPtr.next;
					continue;
				}
				if(!visited[friendPtr.fnum]&&friendsch.equals(school)) {
					Q.enqueue(g.members[friendPtr.fnum]);
					visited[friendPtr.fnum] = true;
				}
				friendPtr = friendPtr.next;
			}
		}
	}
	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {
		
		ArrayList<String> c = new ArrayList<String>();
		int tot = g.members.length;
		boolean[] visited = new boolean[tot];
		boolean[] backed = new boolean[tot];
		int[] dfsnum = new int[tot];
		int[] back = new int [tot];
		
		for(int i = 0; i < tot; i++) {
			if(!visited[i]) {
				DFS(g, i, i, i, visited, backed, dfsnum, back, c);
			}
		}
		
		return c;
	}
	
	private static void DFS(Graph g, int curr, int prevIndex, int startIndex, boolean[] visited, boolean[] backed, int[] dfsnum, int back[], ArrayList<String> c) {
		if(visited[curr]) {
			return;
		}
		visited[curr] = true;
		dfsnum[curr] = dfsnum[prevIndex]+1;
		back[curr] = dfsnum[curr];
		Friend friendPtr = g.members[curr].first;
		while(friendPtr != null) {
			if(visited[friendPtr.fnum]) {
				back[curr] = Math.min(dfsnum[friendPtr.fnum], back[curr]);
			}else {
				DFS(g, friendPtr.fnum, curr, startIndex, visited, backed, dfsnum, back, c);
				if(dfsnum[curr] <= back[friendPtr.fnum] && ! c.contains(g.members[curr].name) && (curr!=startIndex||backed[curr]==true)) {
					c.add(g.members[curr].name);
				}
				if(dfsnum[curr] > back[friendPtr.fnum]) {
					back[curr] = Math.min(dfsnum[curr], back[friendPtr.fnum]);
				}
				backed[curr] = true;
			}
			friendPtr = friendPtr.next;
		}
	}
}

