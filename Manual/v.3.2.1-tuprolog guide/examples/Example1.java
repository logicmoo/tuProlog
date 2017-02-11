    import alice.tuprolog.*;

    public class Example1 {
        public static void main(String[] args) throws Exception {
            Prolog engine = new Prolog();
            SolveInfo info = engine.solve("append([1],[2,3],X).");
            System.out.println(info.getSolution());
        }
    }
