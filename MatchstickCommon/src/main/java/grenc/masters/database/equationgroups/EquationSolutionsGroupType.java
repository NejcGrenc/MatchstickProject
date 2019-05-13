package grenc.masters.database.equationgroups;

public enum EquationSolutionsGroupType
{
	group_1N_2N_3N,
	group_1N_2N,
	group_1N,
	group_1N_2MO,
	group_1N_2MA,
	group_1MA,
	group_1MO,
	group_1O,
	group_1O_2MO,
	group_1O_2MA,
	group_1O_2O,
	group_1O_2O_3O,
	group_1X_2N_3N,
	group_1X_2N,
	group_1X_2MA,
	group_1X_2MO,
	group_1X_2O,
	group_1X_2O_3O
	;
	
	
	private final EquationDAO dao;
	
	private EquationSolutionsGroupType()
	{
		this.dao = new EquationDAO(this.name());
	}
	
	public EquationDAO dao()
	{
		return this.dao;
	}
}
