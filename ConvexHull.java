import java.util.*;

// @pjs preload must be used to preload the image

/* @pjs preload="/scripts/ConvexHull/play.jpg"; */
/* @pjs preload="/scripts/ConvexHull/pause.jpg"; */

public static final int delay_millis = 300;
public static boolean computing = false;
public static int _step = 0;
public static int step_framerate = 20;
public static int _sfr = 0;

public boolean paused = true;
public Button play, pause;
public ArrayList<Button> buttons;
//PImage play_img, pause_img;
PShape play_sh, pause_sh,p1,p2;
int min_uix, max_uix, min_uiy, max_uiy; 

void _delay(int delay)
{
  //  int time = millis();
  //  while(millis() - time <= delay);
}

void setup() {
  size(800, 400, P2D);
  strokeWeight(10);
  background(255);

  play = new Button(10, 10, 25, 25, "play");
  pause = new Button(50, 10, 25, 25, "pause");
  buttons = new ArrayList<Button>();
  buttons.add(play);
  buttons.add(pause);

  min_uix = width;
  max_uix = 0;
  min_uiy = height;
  max_uiy = 0;
  for (Button btn : buttons) {
    if (btn.x < min_uix)
      min_uix = btn.x;
    if (btn.y < min_uiy)
      min_uiy = btn.y;

    if (btn.x + btn.width > max_uix)
      max_uix = btn.x  + btn.width;
    if (btn.y + btn.height > max_uiy)
      max_uiy = btn.y  + btn.height;
  }
 
  play_sh = createShape(TRIANGLE, 5, 5, 20, 12.5, 5,20 );
  play_sh.setFill(color(0, 0, 0));
  play_sh.setStroke(false);
   pause_sh = createShape(GROUP);

  p1 = createShape();
  p1.beginShape();
  p1.fill(0, 0, 0);
  p1.noStroke();
  p1.vertex(5, 5);
  p1.vertex(5, 20);
  p1.vertex(10, 20);
  p1.vertex(10, 5);
  p1.endShape(CLOSE);
  
  p2 = createShape();
  p2.beginShape();
  p2.fill(0, 0, 0);
  p2.noStroke();
  p2.vertex(15, 5);
  p2.vertex(15, 20);
  p2.vertex(20, 20);
  p2.vertex(20, 5);
  p2.endShape(CLOSE);
  pause_sh.addChild(p1);
  pause_sh.addChild(p2);
  //play_img = loadImage("C:/Users/amir/ConvexHull Web Project/scripts/ConvexHull/play.jpg");
  //pause_img = loadImage("C:/Users/amir/ConvexHull Web Project/scripts/ConvexHull/pause.jpg");
}

boolean flag = false;
ArrayList<Point> points = new ArrayList<Point>();
void draw() {
  if (mousePressed && !flag) {
    if (!uiClick(mouseX, mouseY)) {
      PVector p = new PVector(mouseX, mouseY);
      points.add(new Point(p.x, p.y));
      point(p.x, p.y);
    } else {
      handleUiClick(mouseX, mouseY);
    }
    flag = true;
  }

  _sfr++;
  if (_sfr > step_framerate) {
    // execute compute CH method at this step
    if (computing && !paused) {
      println("Running step " + _step + "...");
      computeConvexHull(_step++);
    }
    _sfr = 0;
  }

drawUI();

}
 public void drawUI() {
  int w = 10;
  strokeWeight(2);
  /*int padding = 5;
  rect(min_uix - padding, min_uiy - padding, max_uix, max_uiy);
  for (Button btn : buttons) {
    if (btn.name == "play") {
      image(play_img, play.x, play.y, play.width, play.height);
    } else if (btn.name == "pause") {
      image(pause_img, pause.x, pause.y, pause.width, pause.height);
    } 
  }
  */
 shape(play_sh, 10, 10);
 shape(pause_sh, 50, 10);
  strokeWeight(w);
}

public boolean uiClick(int x, int y) {
  int margin = 15;
  if (x > min_uix - margin && x < max_uix + margin) {
    if (y > min_uiy - margin && y < max_uiy + margin) {
      return true;
    }
  }
  return false;
}

public void handleUiClick(int x, int y) {
  for (Button btn : buttons) {
    if (btn.isClicked(x, y)) {
      if (btn.name == "play") {
        paused = false;
        if (!computing) {
          background(255);
          strokeWeight(3);
          computing = true;
        }
      } else if (btn.name == "pause") {
        paused = true;
        println("Paused...");
      }
      return;
    }
  }
}

void mouseReleased() {
  flag = false;
}

void keyPressed() {
  if (key == ENTER) {
    background(255);
    strokeWeight(3);
    //    for (int i = 1; i < points.size () + 1; i++) {
    //      line(points.get((i-1)%points.size()).x, points.get((i-1)%points.size()).y, points.get(i%points.size()).x, points.get(i%points.size()).y);
    //    }
    computing = true;
    paused = false;
    // prompt for running the algorithm
    //      point(mouseX, mouseY);
  } else if (key == ESC) {
    flag = false;
    points = new ArrayList<Point>();
    background(255);
    paused = true;
  }
}

// case 0
Point[] _points;
// case 1
Graph g;
int n;
ArrayList<Point> Lupper;
Stack<Edge> edges;
Stack<String> CH;
int LUsize;
// default
int _i1;
boolean passed_initial1 = false;
ArrayList<Point> Llower;

boolean passed_initial2 = false;
boolean passed_initial3 = false;
int LLsize;
int _i2;

void computeConvexHull(int step) {

  switch (step) {
  case 0:
    //  Point[] _points = new Point[points.size()];
    _points = new Point[points.size()];
    for (int i =0; i<points.size (); i++) {
      _points[i] = points.get(i);
    }
    for (int i=0; i<_points.length; i++) {
      for (int j=i+1; j<_points.length; j++) {
        int ret = _points[i].compareTo(_points[j]);
        switch (ret) {
        case 1: // means _points[i] is on the right (larger) >> swap
          Point p = new Point(_points[i].x, _points[i].y);
          _points[i] = _points[j];
          _points[j] = p;
          break;
        case -1: // okay
          break;
        default: // do nothing
          break;
        }
      }
    }

    strokeWeight(10);
    stroke(0, 0, 0);
    for (Point p : points) {
      point(p.x, p.y);
    }
    break;

  case 1:
    //  Graph g = new Graph();
    g = new Graph();

    //  int n = points.size();
    n = points.size();

    if (n < 2)
      return;

    // TODO : step("Computing upper part of the convex hull (Lupper)");//Computing upper part of the convex hull (Lupper)

    // Lupper shall contain a set of points all on the convex hull
    //  ArrayList<Point> Lupper = new ArrayList<Point>();
    Lupper = new ArrayList<Point>();

    //  Stack<Edge> edges = new Stack<Edge>();
    //  Stack<String> CH = new Stack<String>(); // holds labels of points on the convex hull
    edges = new Stack<Edge>();
    CH = new Stack<String>(); // holds labels of points on the convex hull

    // Adding p_1 & p_2 as the first possible two points
    //TODO : step("Adding p_1 to Lupper");//Adding p_1 to Lupper
    Lupper.add(_points[0]);
    CH.add(_points[0].getLabel());

    //TODO : step("Adding p_2 to Lupper");//Adding p_2 to Lupper
    // Adding p_2 to Lupper
    Lupper.add(_points[1]);
    CH.add(_points[1].getLabel());


    Edge edge = new Edge(_points[0], _points[1]);
    g.insertEdge(edge).draw();

    edges.add(edge);

    // (p_1, p_2) might be an edge of the convex hull
    // TODO : step("(p_1, p_2) might be an edge of the convex hull");//(p_1, p_2) might be an edge of the convex hull
    LUsize = Lupper.size();
    _i1 = 2;
    break;

  default:

    if (_i1 < points.size()) {
      //    for (int i = 2; i < points.size (); i++) {
      if (!passed_initial1) {
        Lupper.add(_points[_i1]);
        CH.add(_points[_i1].getLabel());
        LUsize++;

        // Adding new edge

        //TODO :  step("p_" + (i + 1) + " might be the next point on convex hull");//p_ + (i + 1) +  might be the next point on convex hull
        edge = new Edge(Lupper.get(LUsize - 2), Lupper.get(LUsize - 1));
        g.insertEdge(edge).draw();
        edges.add(edge);

        passed_initial1 = true;
        return;
      }

      // Removing violating edges
      //      while (LUsize > 2 && !isOnRight (Lupper.get (LUsize - 3), Lupper.get(LUsize - 2), Lupper.get(LUsize - 1))) {
      if (LUsize > 2 && !isOnRight (Lupper.get (LUsize - 3), Lupper.get(LUsize - 2), Lupper.get(LUsize - 1))) {
        //TODO :  step("Last three points do not make a right turn > + the middle one cannot be on the convex hull");//Last three points do not make a right turn > + the middle one cannot be on the convex hull
        g.removeEdge(edges.pop()).clear(g);
        g.removeEdge(edges.pop()).clear(g);

        Lupper.remove(LUsize-- - 2);

        String lastPlbl = CH.pop();
        CH.pop(); // Removing middle point index
        CH.add(lastPlbl);

        // Adding new edge
        // TODO :  step("Inserting next possible edge of the convex hull");//Inserting next possible edge of the convex hull

          edge = new Edge(Lupper.get(LUsize - 2), Lupper.get(LUsize - 1));
        g.insertEdge(edge).draw();
        edges.add(edge);

        return;
      }

      _i1++;
      passed_initial1 = false;
      return;
    }

    // Removing marks from last two points

    // TODO :  step("Computing lower part of the convex hull (Llower)");//Computing lower part of the convex hull (Llower)
    if (!passed_initial2) {
      Llower = new ArrayList<Point>();

      edges = new Stack<Edge>();

      // Adding p_n & p_(n-1) as the first possible two points
      // TODO :  step("Adding p_" + n + " to Llower");//Adding p_" + n + " to Llower
      Llower.add(_points[n - 1]);
      if (CH.lastElement() != _points[n - 1].getLabel()) {
        CH.add(_points[n - 1].getLabel());
      }
      // TODO :  step("Adding p_" + (n - 1) + " to Llower"); //Adding p_ + (n - 1) + to Llower
      Llower.add(_points[n - 2]);
      CH.add(_points[n - 2].getLabel());
      edge = new Edge(_points[n - 1], _points[n - 2]);
      g.insertEdge(edge).draw();
      edges.add(edge);
      // TODO :  step("(p_" + n + ", p_" + (n - 1) + ") might be a lower edge of the convex hull");//(p_" + n + ", p_" + (n - 1) + ") might be a lower edge of the convex hull
      LLsize = Llower.size();
      _i2 = n - 3;

      passed_initial2 = true;
      return;
    }

    if (_i2 >= 0) {
      //    for (int i = n - 3; i >= 0; i--) {
      if (!passed_initial3) {
        Llower.add(_points[_i2]);
        CH.add(_points[_i2].getLabel());
        LLsize++;
        // mark points[i]

        // Adding new edge
        // TODO :  step("p_" + (i + 1) + " might be the next point on lower part of the convex hull");//p_" + (i + 1) + " might be the next point on lower part of the convex hull
        edge = new Edge(Llower.get(LLsize - 2), Llower.get(LLsize - 1));
        g.insertEdge(edge).draw();
        edges.add(edge);

        passed_initial3 = true;
        return;
      }

      // Removing violating edges
      //      while (LLsize > 2 && !isOnRight (Llower.get (LLsize - 3), Llower.get(LLsize - 2), Llower.get(LLsize - 1))) {
      if (LLsize > 2 && !isOnRight (Llower.get (LLsize - 3), Llower.get(LLsize - 2), Llower.get(LLsize - 1))) {
        //TODO :  step(" Last three points do not make a right turn > +the middle one cannot be on the convex hull");// Last three points do not make a right turn > +the middle one cannot be on the convex hull
        edge = edges.pop();
        g.removeEdge(edge).clear(g);
        edge = edges.pop();
        g.removeEdge(edge).clear(g);

        Llower.remove(LLsize-- - 2);

        String lastPlbl = CH.pop();
        CH.pop(); // Removing middle point index
        CH.add(lastPlbl);

        // Adding new edge
        // TODO :  step("Inserting next possible edge of the convex hull");//Inserting next possible edge of the convex hull
        // mark Llower.get(LLsize - 2)
        edge = new Edge(Llower.get(LLsize - 2), Llower.get(LLsize - 1));
        g.insertEdge(edge).draw();
        edges.add(edge);

        return;
      }

      _i2--;
      passed_initial3 = false;
      return;
    }

    // Removing marks from last two points
    // TODO :  step("Removing first and last point from lower part to avoid duplication.");//Removing first and last point from lower part to avoid duplication
    Llower.remove(0);
    Llower.remove(LLsize - 1 - 1);

    // TODO :  step("Appending lower part to the upper part > Resulting list is the list of points on convex hull in clockwise order");//Appending lower part to the upper part > Resulting list is the list of points on convex hull in clockwise order
    Lupper.addAll(Llower);

    //        String s = "";
    //        while (!CH.isEmpty()) {
    //            s += CH.firstElement() + ", ";
    //            CH.removeElementAt(0);
    //        }
    //        s = s.substring(0, s.length() - 3);
    //   println("Points on the convex hull in clockwise order are CH = " + s);
    // TODO : step("Points on the convex hull in clockwise order are CH = " + s); //"Points on the convex hull in clockwise order are CH = " + s

    g.draw();

    //      for (int i = 1; i < points.size () + 1; i++) {
    //      line(points.get((i-1)%points.size()).x, points.get((i-1)%points.size()).y, points.get(i%points.size()).x, points.get(i%points.size()).y);
    //    }

    computing = false;
    _step = 0;  
    strokeWeight(10);
    passed_initial2 = false;
    
    println("Finished!");
    break;
  }
}


private static int _c = 0;
public class Point extends PVector implements Comparable<Point>
{
  private String label;

  public Point(float x, float y) {
    this.label = _c++ + "";

    this.x = x;
    this.y = y;
  }

  public boolean equals(Point other) {
    return other.x == this.x && other.y == this.y;
  }

  public String getLabel() {
    return label;
  }

  public void draw() {
    strokeWeight(10);
    stroke(0, 0, 0);
    point(this.x, this.y);
  }

  public int compareTo(Point that)
  {
    double v1x = this.x, 
    v1y = this.y, 
    v2x = that.x, 
    v2y = that.y;

    int ret = 0;
    if (v1x > v2x) {
      ret = 1;
    } else if (v1x < v2x) {
      ret = -1;
    } else {
      if (v1y < v2y) {
        ret = 1;
      } else if (v1y > v2y) {
        ret = -1;
      } else {
        ret = 0;
      }
    }

    return ret;
  }
}


public class Edge {
  Point v1, v2;
  public Edge(Point v1, Point v2) {
    this.v1 = v1;
    this.v2 = v2;
  }

  public boolean equals(Edge other) {
    return ((other.v1.equals(this.v1) && other.v2.equals(this.v2)) || (other.v1.equals(this.v2) && other.v2.equals(this.v1)));
  }

  public void draw() {
    strokeWeight(3);
    stroke(0);
    line(v1.x, v1.y, v2.x, v2.y);
    _delay(delay_millis);
  }

  public void clear(Graph g) {
    for (Edge e : g.edges) {
      if (e.equals(this))
        return;
    }

    stroke(255); // This has to be the background color
    strokeWeight(5);
    line(v1.x, v1.y, v2.x, v2.y);
    v1.draw();
    v2.draw();
    _delay(delay_millis);
  }
}

public class Graph {
  public ArrayList<Edge> edges;

  public Graph() {
    this.edges = new ArrayList<Edge>();
  }

  public Edge insertEdge(Edge e) {
    this.edges.add(e);
    return e;
  }


  public Edge removeEdge(Edge e) {
    this.edges.remove(e);
    return e;
  }

  public void draw() {
    for (Edge e : edges) {
      e.draw();
    }
  }
}


public boolean isOnRight(Point p, Point q, Point r) {
  // (qx ry - qy rx) - (px ry - py rx) + (px qy - py qx)
  return (q.x * r.y - q.y * r.x)
    - (p.x * r.y - p.y * r.x)
      + (p.x * q.y - p.y * q.x) > 0;
}

//  public class Sort<T> {  
//    
//    //call mergesort on param list
//  public List<T> mergesort(List<T> ilist) { 
//   if(ilist.size() <= 1) {
//      return ilist;
//    } else { 
//      List<T> left = new ArrayList<T>();
//      List<T> right = new ArrayList<T>();
//      
//      int middle = ilist.size() / 2; //int division
//      for(int i=0;i<middle;i++) { 
//        left.add(ilist.get(i));
//      }
//      for(int i=middle;i<ilist.size();i++) { 
//        right.add(ilist.get(i));
//      }
//  
//      
//      return merge(mergesort(left), mergesort(right));
//    }
//  }
//  
//  
//  
//  //used by mergesort to do merging
//  private List<T> merge(List<T> a, List<T> b) { 
//    List<T> ret = new ArrayList<T>();  //return list
//    int a_idx = 0, b_idx = 0;      //counters of items left in respective lists
//    
//    while(a_idx+1 <= a.size() || b_idx+1 <= b.size()) { 
//      if(a_idx+1 <= a.size() && b_idx+1 <= b.size()) {
//        if(a.get(a_idx).compareTo(b.get(b_idx)) <= 0.0) { 
//          ret.add(a.get(a_idx));
//          a_idx++;
//        } else { 
//          ret.add(b.get(b_idx));
//          b_idx++;
//        }
//      } else if(a_idx+1 <= a.size()) { 
//        ret.add(a.get(a_idx));
//        a_idx++;
//      } else if(b_idx+1 <= b.size()) { 
//        ret.add(b.get(b_idx));
//        b_idx++;
//      }
//    }
//   
//    
//    return ret;  
//  }
//  
//  }


//    
//    public static Comparable[] mergeSort(Comparable[ ] a)
//  {
//    Comparable[] tmp = new Comparable[a.length];
//    return mergeSort(a, tmp,  0,  a.length - 1);
//  }
//
//
//  private static Comparable[] mergeSort(Comparable [ ] a, Comparable [ ] tmp, int left, int right)
//  {
//    if( left < right )
//    {
//      int center = (left + right) / 2;
//      mergeSort(a, tmp, left, center);
//      mergeSort(a, tmp, center + 1, right);
//      return merge(a, tmp, left, center + 1, right);
//    }
//    return a;
//  }
//
//
//    private static Comparable[] merge(Comparable[ ] a, Comparable[ ] tmp, int left, int right, int rightEnd )
//    {
//        int leftEnd = right - 1;
//        int k = left;
//        int num = rightEnd - left + 1;
//
//        while(left <= leftEnd && right <= rightEnd)
//            if(a[left].compareTo(a[right]) <= 0)
//                tmp[k++] = a[left++];
//            else
//                tmp[k++] = a[right++];
//
//        while(left <= leftEnd)    // Copy rest of first half
//            tmp[k++] = a[left++];
//
//        while(right <= rightEnd)  // Copy rest of right half
//            tmp[k++] = a[right++];
//
//        // Copy tmp back
//        for(int i = 0; i < num; i++, rightEnd--)
//            a[rightEnd] = tmp[rightEnd];
//            
//        return a;
//    }



public class Stack<T> {
  private int maxSize;

  private ArrayList<T> stackArray;

  private int top;

  public Stack() {
    stackArray = new ArrayList<T>();
    top = -1;
  }

  public void add(T j) {
    this.push(j) ;
  }

  public void push(T j) {
    top++;
    stackArray.add(j);
  }

  public T pop() {
    T elem = stackArray.get(top--);
    removeElementAt(top+1);
    return elem;
  }

  public void removeElementAt(int i) {
    stackArray.remove(i);
  }

  //  public T firstElement() {
  //     return stackArray.get(0); 
  //  }

  public T lastElement() {
    return stackArray.get(top);
  }

  public boolean isEmpty() {
    return (top == -1);
  }
}

public class Button {
  int x, y;
  int width, height;
  String name;

  public Button(int x, int y, int width, int height, String name) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.name = name;
  }

  public boolean isClicked(int x, int y) {
    if (x > this.x && x < this.x + this.width) {
      if (y > this.y && y < this.y + this.height) {
        return true;
      }
    }
    return false;
  }
}

//
//
//static class MergeSorter {
//
//  public static <E> void sort(E[] a, Comparator<? super E> comp) {
//    mergeSort(a, 0, a.length - 1, comp);
//  }
//
//
//  private static <E> void mergeSort(E[] a, int from, int to, Comparator<? super E> comp) {
//    if (from == to)
//      return;
//    int mid = (from + to) / 2;
//    // Sort the first and the second half
//    mergeSort(a, from, mid, comp);
//    mergeSort(a, mid + 1, to, comp);
//    merge(a, from, mid, to, comp);
//  }
//
//  private static <E> void merge(E[] a, int from, int mid, int to, Comparator<? super E> comp) {
//    int n = to - from + 1;
//    Object[] values = new Object[n];
//
//    int fromValue = from;
//
//    int middleValue = mid + 1;
//
//    int index = 0;
//
//    while (fromValue <= mid && middleValue <= to) {
//      if (comp.compare(a[fromValue], a[middleValue]) < 0) {
//        values[index] = a[fromValue];
//        fromValue++;
//      } else {
//        values[index] = a[middleValue];
//        middleValue++;
//      }
//      index++;
//    }
//
//    while (fromValue <= mid) {
//      values[index] = a[fromValue];
//      fromValue++;
//      index++;
//    }
//    while (middleValue <= to) {
//      values[index] = a[middleValue];
//      middleValue++;
//      index++;
//    }
//
//    for (index = 0; index < n; index++)
//      a[from + index] = (E) values[index];
//  }
//}
