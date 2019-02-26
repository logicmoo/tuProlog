/*Castagna 06/2011*/
package alice.tuprolog;

public interface TuTermVisitor {
	void visit(TuStruct s);
	void visit(TuVar v);
	void visit(TuNumber n);
}
/**/