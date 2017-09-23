package toolbox;

import java.util.List;
import java.util.Random;


public class Maths {
	
	public static Random random = new Random();
	
	public static int irand(int max){
		return random.nextInt(max);
	}
	
	public static float frand(float max){
		return random.nextFloat() * max;
	}
	
	public static int max(List<Integer> iList){
		if(iList == null) return 0;
		
		int max = iList.get(0);
		for(int i = 1; i < iList.size(); i++){
			if(iList.get(i) > max){
				max = iList.get(i);
			}
		}
		return max;
	}
	

}
