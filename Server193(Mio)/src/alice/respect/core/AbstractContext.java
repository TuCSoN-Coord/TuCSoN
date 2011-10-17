package alice.respect.core;

import alice.logictuple.LogicTuple;
import alice.respect.api.IRespectTC;

/*insert by Enrico Rossignoli*/
/*
 * a new abstract class for the context
 */
public abstract class AbstractContext {
	
    private IRespectTC core;
    
    public AbstractContext(IRespectTC core){
    	this.core = core;
    }
    
    protected IRespectTC getCore(){
    	return core;
    }
    
	protected LogicTuple unify(alice.tuplecentre.api.TupleTemplate template,alice.tuplecentre.api.Tuple tuple){
        boolean res=template.propagate(core.getVM().getRespectVMContext().getPrologCore(), tuple);
        if (res){
            return (LogicTuple)template;
        } else {
            return null;
        }
    }

}
