    import alice.tuprolog.*;

    public class Example1b {
        public static void main(String[] args) throws Exception {
            Prolog engine = new Prolog();
            SolveInfo info = engine.solve("append(X,Y,[1,2]).");
            while (info.isSuccess()) {
                System.out.println("solution: " + info.getSolution() +
                                   " - bindings: " + info);
                if (engine.hasOpenAlternatives()) {
                    info = engine.solveNext();
                } else {
                    break;
                }
            }
        }
    }
