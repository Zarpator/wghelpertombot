package servicelayer.receiving;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class getMeAnswer {
	private boolean ok;
	private Result result;
	
	@Override
	public String toString(){
		return "" + ok;
	}
}
