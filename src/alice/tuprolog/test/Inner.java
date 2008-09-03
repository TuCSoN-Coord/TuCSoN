package alice.tuprolog.test;

class MyInner extends Inner {
	public void test(){
		System.out.println("ok.");
	}
}

public abstract class Inner {

	abstract public void test();
	
	public static Inner make(){
		return new MyInner();
	}

}

