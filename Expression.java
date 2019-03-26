import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {

	public static String delims = " \t*+-/()[]";
			
    /**
     * Populates the vars list with simple variables, and arrays lists with arrays
     * in the expression. For every variable (simple or array), a SINGLE instance is created 
     * and stored, even if it appears more than once in the expression.
     * At this time, values for all variables and all array items are set to
     * zero - they will be loaded from a file in the loadVariableValues method.
     * 
     * @param expr The expression
     * @param vars The variables array list - already created by the caller
     * @param arrays The arrays array list - already created by the caller
     */
    public static void 
    makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	StringTokenizer s = new StringTokenizer(expr,delims);
    	String str = "";
    	int index = 0;
    	int size = 0;
    	while (s.hasMoreTokens()) {
    		str=s.nextToken();
			size = str.length();
			index = expr.indexOf(str);
			if(expr.indexOf(str)!=expr.lastIndexOf(str)) {
				if(index == 0) {
					while(Character.isLetter(expr.charAt(index+size))) {
						index = expr.indexOf(str,index+1);
					}
				}else {
					while(Character.isLetter(expr.charAt(index-1))||Character.isLetter(expr.charAt(index+size))) {
						index = expr.indexOf(str,index+1);
					}
				}
			}
    		if(Character.isLetter(str.charAt(0))) {
    			if (index + size + 1 > expr.length()){
					vars.add(new Variable(str));
					break;
				}else if(expr.charAt(index+size)=='[') {
					Array tempA = new Array(str);
					if (arrays.contains(tempA)){
						continue;
					}
					arrays.add(new Array(str));
    			}else{
    				Variable tempv = new Variable(str);
					if (vars.contains(tempv)){
						continue;
					}
    				vars.add(new Variable(str));
    			}
    		}
    	}
    }
    
    /**
     * Loads values for variables and arrays in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     * @param vars The variables array list, previously populated by makeVariableLists
     * @param arrays The arrays array list - previously populated by makeVariableLists
     */
    public static void 
    loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String tok = st.nextToken();
            Variable var = new Variable(tok);
            Array arr = new Array(tok);
            int vari = vars.indexOf(var);
            int arri = arrays.indexOf(arr);
            if (vari == -1 && arri == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar symbol
                vars.get(vari).value = num;
            } else { // array symbol
            	arr = arrays.get(arri);
            	arr.values = new int[num];
                // following are (index,val) pairs
                while (st.hasMoreTokens()) {
                    tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    arr.values[index] = val;              
                }
            }
        }
    }
    
    /**
     * Evaluates the expression.
     * 
     * @param vars The variables array list, with values for all variables in the expression
     * @param arrays The arrays array list, with values for all array items
     * @return Result of evaluation
     */
    public static float 
    evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	expr.trim();
    	while (expr.contains("(")){
    		int posst = 0;
    		int posen = 0;
    		for(int i = 0; i < expr.length();i++) {
    			if(expr.charAt(i)=='(') {
    				posst = i;
    			}
    			if(expr.charAt(i)==')') {
    				posen = i;
    				break;
    			}
    		}
    		
    		String ins = expr.substring(posst+1, posen);
    		if(posst-1>0&&posen+1<expr.length()) {
	    			while(expr.charAt(posst-1)=='('&&expr.charAt(posen+1)==')') {
	    				expr = expr.substring(0,posst)+ins+expr.substring(posen+1,expr.length());
	    				posst--;
	            		posen--;
	            		if(posst==0||posen==expr.length()-1) {
	            			break;
	            		}
	        		}
    		}
    		int m = (int)evaluate(ins,vars,arrays);
	   		if(posen>=expr.length()) {
				expr = expr.substring(0,posst)+m;
			}else {
				expr = expr.substring(0,posst)+m+ expr.substring(posen+1, expr.length());
			}	
		}
    	int possta = 0;
		int posend = 0;
		String ins = "";
    	if(expr.contains("[")) {
			possta = 0;
			posend = 0;
			ins = "";
	    	do{
	    		for(int i = 0; i < expr.length();i++) {
	    			if(expr.charAt(i)=='[') {
	    				possta = i;
	    			}
	    			if(expr.charAt(i)==']') {
	    				posend = i;
	    				ins = expr.substring(possta+1, posend);
	    	    		if(isnum(ins)) {
	    		    		float arrayval = 0;
	    		    		StringTokenizer st1 = new StringTokenizer(expr, delims, true);
	    	    	    	String next1 = st1.nextToken();
	    	    	    	String next2 = st1.nextToken();
	    	    	    	String next3 = st1.nextToken();
	    	    	    	int length = 0;
	    	    	    	while(st1.hasMoreTokens()) {
	    	    	    		if(next2.equals("[")&& expr.indexOf(ins)==expr.indexOf(next3)) {
	    	    	    			arrayval = getarr(next1,(int)getnum(next3),arrays);
	    		    	    		break;
	    	    	    		}
	    	    	    		length = length+next1.length();
	    	    	    		next1 = next2;
	    	    	    		next2 = next3;
	    	    	    		next3 = st1.nextToken();
	    	    	    	}
	    	    	    	if(posend>=expr.length()) {
	    						expr = expr.substring(0,length)+(int)arrayval;
	    					}else {
	    						expr = expr.substring(0,length)+(int)arrayval+ expr.substring(posend+1, expr.length());
	    					}
	    				}else {
	    		    		expr = expr.substring(0,possta+1)+(int)evaluate(ins,vars,arrays)+ expr.substring(posend, expr.length());
	    				}
	    				break;
	    			}
	    		}
	    		
	    	}while(expr.contains("[")) ;
    	}
    	
    	
    	//only a number/variable
    	if (isnum(expr)) {
    		return getnum(expr);
    	}else if(isvar(expr)) {
    		return getvar(expr,vars);
    	}
    	
    	
    	StringTokenizer st2 = new StringTokenizer(expr, delims, true);
    	String next = st2.nextToken();;
    	Stack<String> operator = new Stack<String>();
    	Stack<Float> value = new Stack<Float>();
		Boolean conti = true;
    	while(conti) {
    		conti = false;
    		if(st2.hasMoreTokens()) {
    			conti = true;
    		}
    		if(next.equals(" ")) {
    			next = st2.nextToken();
    		}
    		if(next.equals("+")||next.equals("-")||next.equals("*")||next.equals("/")) {
    			operator.push(next);
    			String nexts = st2.nextToken();
    			if(nexts.equals("-")) {
    				next = st2.nextToken();
    				next = nexts+next;
        			value.push(getnum(next));
    				if(st2.hasMoreTokens()) {
    		    		next = st2.nextToken();
    	    		}else {
    	    			break;
    	    		}
    			}else {
    				next  = nexts;
    			}
    			conti = true;
    			continue;
    		}else if (isnum(next)) {
    			value.push(getnum(next));
    		}else {
    			value.push(getvar(next,vars));
    		}
    		if(st2.hasMoreTokens()) {
	    		next = st2.nextToken();
    		}
    	}
    	Stack<Float> temval = new Stack<Float>();
		Stack<String> temope = new Stack<String>();
		return calculate(value,operator,temval,temope);
    }

    private static float calculate(Stack<Float> val, Stack<String> op,Stack<Float> temva, Stack<String> temop) {
    	float val1 = val.pop();
		float val2 = val.pop();
		String o = op.pop();
		do {
			if(o.equals("/")){
				temva.push(val2/val1);
			}else if(o.equals("*")){
				temva.push(val2*val1);
			}else {
				if(!op.isEmpty()) {
					while(op.peek().equals("/")||op.peek().equals("*")||op.peek().equals("-")) {
						temva.push(val1);
						temop.push(o);
						val.push(val2);
						val2 = calculate(val,op,temva,temop);
						val1 = temva.pop();
						o = temop.pop();
						if(op.isEmpty()) {
							break;
						}
					}
				}
				if(o.equals("+")){
					temva.push(val2+val1);
				}
				if (o.equals("-")){
					temva.push(val2-val1);
				}
			}
			if(op.isEmpty()) {
				break;
			}
			val2 = val.pop();
			val1 = temva.pop();
			o = op.pop();
		}while(o!=null);
		return temva.pop();
    }
    
    
    private static boolean isnum(String expres) {
    	for(int i = 0; i < expres.length();i++) {
    		if(!Character.isDigit(expres.charAt(i))) {
    			return false;
    		}
    	}
    	return true;
    }
    private static boolean isvar(String expres) {
    	if(expres.contains("+") || expres.contains("-") || expres.contains("/") || expres.contains("*") || expres.contains("[")) {
			return false;
		}
    	return true;
    }
    private static float getnum(String expres) {
    	return Float.parseFloat(expres);
    }
    private static float getvar(String expres, ArrayList<Variable> varis) {
    	int index = 0;
		for(int i = 0; i < varis.size(); i++){
			if(varis.get(i).name.equals(expres)){
				index = i;
			}
		}
		return varis.get(index).value;
    }
    private static float getarr(String expres, int index, ArrayList<Array> arrs) {
    	int k = 0;
		for(int i = 0; i < arrs.size(); i++){
			if(arrs.get(i).name.equals(expres)){
				k = i;
			}
		}
		return arrs.get(k).values[index];
    }
}
