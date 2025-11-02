package graph;

public class DatasetGenerator {
    public Graph generateSmallGraph(int variant) {
        switch (variant) {
            case 1: {
                int n = 8;
                Graph g = new Graph(n, true, "integer");
                g.addEdge(0,1,2); g.addEdge(0,2,1); g.addEdge(1,3,3); g.addEdge(2,3,1);
                g.addEdge(3,4,2); g.addEdge(2,5,4); g.addEdge(5,6,1); g.addEdge(4,7,5);
                return g;
            }
            case 2: {
                int n = 8;
                Graph g = new Graph(n, true, "integer");
                g.addEdge(0,1,2); g.addEdge(1,2,2); g.addEdge(2,0,1);
                g.addEdge(2,3,3); g.addEdge(3,4,1); g.addEdge(4,5,2); g.addEdge(5,6,1); g.addEdge(6,7,2);
                return g;
            }
            case 3: {
                int n = 10;
                Graph g = new Graph(n, true, "integer");
                g.addEdge(0,1,1); g.addEdge(1,0,1);
                g.addEdge(3,4,2); g.addEdge(4,5,2); g.addEdge(5,3,2);
                g.addEdge(2,3,1); g.addEdge(6,7,3); g.addEdge(7,8,2); g.addEdge(8,9,4);
                g.addEdge(2,6,1); g.addEdge(5,9,5);
                return g;
            }
            default: throw new IllegalArgumentException("variant must be 1..3");
        }
    }

    public Graph generateMediumGraph(int variant) {
        switch (variant) {
            case 1: {
                int n = 15;
                Graph g = new Graph(n, true, "integer");
                g.addEdge(0,1,1); g.addEdge(1,2,2); g.addEdge(2,3,2); g.addEdge(3,4,1);
                g.addEdge(1,5,3); g.addEdge(5,6,2); g.addEdge(6,2,1);
                g.addEdge(4,7,4); g.addEdge(7,8,1); g.addEdge(8,9,2);
                g.addEdge(5,10,2); g.addEdge(10,11,3); g.addEdge(11,12,1);
                g.addEdge(9,13,2); g.addEdge(12,14,5);
                return g;
            }
            case 2: {
                int n = 18;
                Graph g = new Graph(n, true, "integer");
                for (int i=0;i<8;i++) g.addEdge(i,i+1,1+i%3);
                g.addEdge(0,2,2); g.addEdge(1,3,2); g.addEdge(2,4,3); g.addEdge(3,5,1);
                g.addEdge(4,9,2); g.addEdge(5,10,2); g.addEdge(6,11,2); g.addEdge(7,12,2);
                g.addEdge(9,13,3); g.addEdge(10,14,1); g.addEdge(11,15,2); g.addEdge(12,16,2);
                g.addEdge(13,17,4); g.addEdge(14,17,2); g.addEdge(15,17,3); g.addEdge(16,17,1);
                return g;
            }
            case 3: {
                int n = 20;
                Graph g = new Graph(n, true, "integer");
                g.addEdge(0,1,1); g.addEdge(1,2,2); g.addEdge(2,0,1);
                g.addEdge(2,3,3); g.addEdge(3,4,2); g.addEdge(4,5,2); g.addEdge(5,3,1);
                g.addEdge(6,7,1); g.addEdge(7,8,2); g.addEdge(8,9,3);
                g.addEdge(4,6,2); g.addEdge(9,10,2); g.addEdge(10,11,1); g.addEdge(11,12,2);
                g.addEdge(12,13,3); g.addEdge(13,14,1); g.addEdge(14,6,2);
                g.addEdge(8,15,2); g.addEdge(15,16,2); g.addEdge(16,17,2); g.addEdge(17,18,2); g.addEdge(18,19,2);
                return g;
            }
            default: throw new IllegalArgumentException("variant must be 1..3");
        }
    }

    public Graph generateLargeGraph(int variant) {
        switch (variant) {
            case 1: {
                int n = 30;
                Graph g = new Graph(n, true, "integer");
                for (int i=0;i<29;i++) g.addEdge(i,i+1,1+(i%5));
                g.addEdge(0,5,3); g.addEdge(5,10,2); g.addEdge(10,15,4); g.addEdge(15,20,2); g.addEdge(20,25,3);
                g.addEdge(3,12,2); g.addEdge(7,18,3); g.addEdge(11,22,2); g.addEdge(14,27,5);
                return g;
            }
            case 2: {
                int n = 40;
                Graph g = new Graph(n, true, "integer");
                for (int i=0;i<20;i++) g.addEdge(i,i+1,1+(i%3));
                g.addEdge(2,0,1); g.addEdge(6,4,1); g.addEdge(12,10,1);
                for (int i=20;i<39;i++) g.addEdge(i,i+1,2+(i%4));
                g.addEdge(21,25,2); g.addEdge(25,28,3); g.addEdge(28,21,1);
                g.addEdge(5,23,2); g.addEdge(9,30,3); g.addEdge(13,35,2);
                return g;
            }
            case 3: {
                int n = 50;
                Graph g = new Graph(n, true, "integer");
                for (int i=0;i<49;i++) g.addEdge(i,i+1,1+(i%7));
                for (int i=0;i<50;i+=5) g.addEdge(i,Math.min(49,i+7),2);
                g.addEdge(8,4,1); g.addEdge(22,18,1); g.addEdge(36,32,1);
                for (int i=10;i<50;i+=10) g.addEdge(i,i-5,3);
                g.addEdge(3,20,2); g.addEdge(15,33,4); g.addEdge(27,41,3); g.addEdge(6,44,5);
                return g;
            }
            default: throw new IllegalArgumentException("variant must be 1..3");
        }
    }
}
