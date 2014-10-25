package com.hack.planets.collision;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

/**
 * Created by danielvaughan on 25/10/14.
 */
public class IntersectionDetector {

   // private Position intersection;

    public Position getIntersection(Movement movementA, Movement movementB)
    {
        Point intersection = calcIntersection(movementA, movementB);
        return new Position(intersection.x, intersection.y);
    }

    private Point calcIntersection(Movement movementA, Movement movementB)
    {
        Position aStart = movementA.getStart();
        Position aEnd = movementA.getEnd();
        Position bStart = movementB.getStart();
        Position bEnd = movementB.getEnd();
        Point p1 = new Point(aStart.getX(), aStart.getY());
        Point p2= new Point(aEnd.getX(), aEnd.getY());
        Point p3 = new Point(bStart.getX(), bStart.getY());
        Point p4 = new Point(bEnd.getX(), bEnd.getY());

        return findIntersection(p1, p2, p3, p4);
    }

    public boolean isCollision(Movement movementA, Movement movementB) {
        return !Float.isNaN(calcIntersection(movementA, movementB).x);
    }

    // calculates intersection and checks for parallel lines.
// also checks that the intersection point is actually on
// the line segment p1-p2
    Point findIntersection(Point p1,Point p2,
                           Point p3,Point p4) {
        float xD1,yD1,xD2,yD2,xD3,yD3;
        float dot,deg,len1,len2;
        float segmentLen1,segmentLen2;
        float ua,ub,div;

        // calculate differences
        xD1=p2.x-p1.x;
        xD2=p4.x-p3.x;
        yD1=p2.y-p1.y;
        yD2=p4.y-p3.y;
        xD3=p1.x-p3.x;
        yD3=p1.y-p3.y;

        // calculate the lengths of the two lines
        len1=(float)sqrt(xD1*xD1+yD1*yD1);
        len2=(float)sqrt(xD2*xD2+yD2*yD2);

        // calculate angle between the two lines.
        dot=(xD1*xD2+yD1*yD2); // dot product
        deg=dot/(len1*len2);

        // if abs(angle)==1 then the lines are parallell,
        // so no intersection is possible
        if(abs(deg)==1) return null;

        // find intersection Pt between two lines
        Point pt=new Point(0,0);
        div=yD2*xD1-xD2*yD1;
        ua=(xD2*yD3-yD2*xD3)/div;
        ub=(xD1*yD3-yD1*xD3)/div;
        pt.x=p1.x+ua*xD1;
        pt.y=p1.y+ua*yD1;

        // calculate the combined length of the two segments
        // between Pt-p1 and Pt-p2
        xD1=pt.x-p1.x;
        xD2=pt.x-p2.x;
        yD1=pt.y-p1.y;
        yD2=pt.y-p2.y;
        segmentLen1=(float)(sqrt(xD1*xD1+yD1*yD1)+sqrt(xD2*xD2+yD2*yD2));

        // calculate the combined length of the two segments
        // between Pt-p3 and Pt-p4
        xD1=pt.x-p3.x;
        xD2=pt.x-p4.x;
        yD1=pt.y-p3.y;
        yD2=pt.y-p4.y;
        segmentLen2=(float)(sqrt(xD1*xD1+yD1*yD1)+sqrt(xD2*xD2+yD2*yD2));

        // if the lengths of both sets of segments are the same as
        // the lenghts of the two lines the point is actually
        // on the line segment.

        // if the point isn't on the line, return null
        if(abs(len1-segmentLen1)>0.01 || abs(len2-segmentLen2)>0.01)
            return null;

        // return the valid intersection
        return pt;
    }

    class Point{
        float x,y;
        Point(float x, float y){
            this.x = x;
            this.y = y;
        }

        void set(float x, float y){
            this.x = x;
            this.y = y;
        }
    }
}
