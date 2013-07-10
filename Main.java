import java.math.BigInteger;
//import java.util.Random;
import java.security.SecureRandom;
import java.lang.instrument.Instrumentation;


public class Main {
	
	// Find if the given Biginteger is odd
	/*
	 * public static boolean isOdd(BigInteger val){
		if(!val.mod(new BigInteger("2")).equals(BigInteger.ZERO))
			return true;
		return false;
	}
	*/
	/*
	private static Instrumentation instrumentation;

    public static void premain(String args, Instrumentation inst) {
        instrumentation = inst;
    }

    public static long getObjectSize(Object o) {
        return instrumentation.getObjectSize(o);
    }
    */
	public static boolean isOne(BigInteger val) {
		if(val.compareTo(BigInteger.ONE) == 0)
			return true;
		return false;
	}
		
	public static BigInteger nextRandomBigInteger(BigInteger n) {
	    SecureRandom rand = new SecureRandom();
	    BigInteger result = new BigInteger(n.bitLength(), rand);
	    while( result.compareTo(n) >= 0 ) {
	        result = new BigInteger(n.bitLength(), rand);
	    }
	    return result;
	}
	
	// The black box to find p, q from n,e,d
	public static boolean pqFromNde(BigInteger n, BigInteger e, BigInteger d, BigInteger p, BigInteger q /*, BigInteger dp, BigInteger dq, BigInteger u*/){
		boolean iResult = false;
		//SecureRandom rand = new SecureRandom();
    	Runtime runtime = Runtime.getRuntime();
    	//getHeap(runtime);
		/*
		BigInteger k = new BigInteger(n.bitLength(),rand);
		BigInteger g = new BigInteger(n.bitLength(),rand);
		BigInteger kt = new BigInteger(n.bitLength(),rand);
		BigInteger rem = new BigInteger(n.bitLength(),rand);
		BigInteger gk = new BigInteger(n.bitLength(),rand);
		BigInteger sq = new BigInteger(n.bitLength(),rand);
		BigInteger Gcd = new BigInteger(n.bitLength(),rand);
		BigInteger temp = new BigInteger(n.bitLength(),rand);
		*/
    	getHeap(runtime,"");
    	long start = System.nanoTime();

		BigInteger k = new BigInteger("1");
		BigInteger g = new BigInteger("1");
		BigInteger kt = new BigInteger("1");
		BigInteger rem = new BigInteger("1");
		BigInteger gk = new BigInteger("1");
		BigInteger sq = new BigInteger("1");
		BigInteger Gcd = new BigInteger("1");
		BigInteger temp = new BigInteger("1");
        //System.out.println(getObjectSize(BigInteger.ONE));

    	//getHeap(runtime,"after object allocation");
    	long end = System.nanoTime();
		//System.out.println("execution time: " + (end - start)/1000000000.0 + "seconds" );
		int i,t; // change it to unsigned long later, using BigInteger http://technologicaloddity.com/2010/09/22/biginteger-as-unsigned-long-in-java/
		k = e.multiply(d);
		k = k.subtract(BigInteger.ONE);
		t =0; // t = BigInteger.ZERO;
		while(!k.testBit(t)) {
			t++;
		}
		while(true) {
			//getHeap(runtime,"in while");
			kt = k.add(BigInteger.ZERO);// find a better way to copy bigint objects. this is silly!
			g = nextRandomBigInteger(n);
			//g = new BigInteger(n.bitLength(), new Random()); 
			for(i=0; i<t; i++) {
				kt =kt.shiftRight(1);
				gk = g.modPow(kt, n);
				if(!isOne(gk)) {
					sq = gk.multiply(gk);
					rem = sq.mod(n);
					if(isOne(rem)) break;
				}
				
			}
            
			if(i < t) {
				gk = gk.subtract(BigInteger.ONE);               
				Gcd = gk.gcd(n);                
				if(!isOne(Gcd)) break;
			}
			
		}
		
		p = Gcd.add(BigInteger.ZERO);
		q = n.divide(p);

		//if(p.isProbablePrime(10) && q.isProbablePrime(10)) { //not necessary, also it is the code bottleneck
			if(q.compareTo(p) > 0) {//swap(p,q) assuming p >q
				temp = p.add(BigInteger.ZERO);
				p = q.add(BigInteger.ZERO);
				q = temp.add(BigInteger.ZERO);
			}
			iResult = true;
			
		//}
		System.out.println("p:" + p.toString(16) + "\n" + "q:" + q.toString(16));
		
        
		return iResult;
	}
	
	public static void main (String args[]){
        
        
		Runtime runtime = Runtime.getRuntime();
		BigInteger n = new BigInteger("00a2fddae519d466a1a22421ee23e1d895675a49c84a8f9a87ae66d60cd7b5b9261afe7a874719822003a1bc4d035e98f0d61284002d162182c0e41c4b0a204141aff1c45c3e16e00369535722416dda73907c5d1acf32c981dec1fe1395ab6986aa0aa612191e0929df3a3d7d62fdfc2b7cb1a8e9d8882839e691244340f764bb",16);
		BigInteger e = new BigInteger("10001",16);
		BigInteger d = new BigInteger("00880b779fee95592ce4976032d38479310b59536878c3b06a3be9de144e20827009ec0afdcb1cc17e5349b65059d5a5b6b0183851a550699c27c0bed4eb98cc9de06b903e0922fde4f7c41e5712e82dd0560ed24745df6fc5cd993884bcf0ffdfd395488d5f579020efcd0bea1b657a2640f9e3cf033cb75367df7315ead56a51",16);
		
		//BigInteger p = new BigInteger(n.bitLength(),new SecureRandom());
		//BigInteger q = new BigInteger(n.bitLength(),new SecureRandom());
		BigInteger p = new BigInteger(BigInteger.ONE.toString());
		BigInteger q = new BigInteger(BigInteger.ONE.toString());
		//getHeap(runtime);

    	long start = System.nanoTime();
		if(pqFromNde(n,e,d,p,q)) {
			//System.out.println(" p :" + p.toString() + " q:" + q.toString() + " n:" + n.toString() + " e:" + e.toString() + " d:" + d.toString());
            //System.out.println("Successful !");
		}
		else {
			System.out.println("time to debug. :( ");
		}
        	 
		long end = System.nanoTime();
		System.out.println("execution time: " + (end - start)/1000000000.0 + "seconds" );
		getHeap(runtime,"End");
        
    
		
	}
	
	
	public static void getHeap(Runtime runtime, String str){
		System.out.println(str + " " + (long)(runtime.totalMemory() - runtime.freeMemory()) + " Bytes" );
		
	}
	
}
