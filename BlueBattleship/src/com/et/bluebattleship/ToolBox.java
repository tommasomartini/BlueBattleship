package com.et.bluebattleship;

public class ToolBox {
	
	private static ToolBox me;
	public boolean primo=false;
	public boolean[] campoVirtuale;
	public boolean[] my_field;
	public boolean[] enemy_field;
	public boolean[] state_enemy_field;
	public boolean[] mancato_enemy;
	public boolean[] colpite;
	public int nemicoVirtuale=0;
	public int numero_navi=1;
	public boolean data_change=false;
	public boolean report_hit=false;
	public int hit;
	public BlueBattleshipService mBlueBattleshipService;
	
	public static ToolBox getInstance(){
		if (me==null) me=new ToolBox();
		return me;
				
	}
	
	private ToolBox(){
		super();
	}
	
	

}
