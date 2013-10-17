package com.et.bluebattleship;

public class ToolBox {
	
	private static ToolBox me;
	public boolean[] campoVirtuale;
	public boolean[] my_field;
	public boolean[] enemy_field;
	public boolean[] mancato_enemy;
	public boolean[] colpite;
	public int nemicoVirtuale=0;
	public int numero_navi=1;
	
	public static ToolBox getInstance(){
		if (me==null) me=new ToolBox();
		return me;
				
	}
	
	private ToolBox(){
		super();
	}

}
