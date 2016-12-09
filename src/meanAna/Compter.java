package meanAna;

import grammerAna.ENode;
import grammerAna.MyDouble;
import view.Compiler;
import view.DrawFrame;

import java.awt.*;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by Yuriko on 2016/12/9.
 */
public class Compter {
    private double originX, originY;
    private double nowX, nowY;
    private double scaleX, scaleY;
    private double rotAngle;
    public MyDouble parameter;
    DrawFrame drawFrame;
    private ArrayList<Point> points = new ArrayList<>();

    public Compter() {
        parameter = new MyDouble(0.0);
        drawFrame = new DrawFrame();
    }

    public static double getExprValue(ENode root) {
        if (root == null) {
            return 0.0;
        }
        switch (root.getType()) {
            case PLUS:
                return (getExprValue(root.getLeft()) + getExprValue(root.getRight()));
            case MINUS:
                return (getExprValue(root.getLeft()) - getExprValue(root.getRight()));
            case MUL:
                return (getExprValue(root.getLeft()) * getExprValue(root.getRight()));
            case DIV:
                return (getExprValue(root.getLeft()) / getExprValue(root.getRight()));
            case POWER:
                return Math.pow(getExprValue(root.getLeft()), getExprValue(root.getRight()));
            case FUNC:
                return getReflectValue(root.getMathFunName(), getExprValue(root.getChild()));
            case CONST_ID:
                return root.getCaseConst();
            case T:
                //System.out.println("T:" + root.getCaseParm().getValue());
                return root.getCaseParm().getValue();
            default:
                return 0.0;
        }
    }

    public void calcuCoord(ENode horNode, ENode verNode) {
        double horCord, verCord, horTemp;
        horCord = getExprValue(horNode);
        verCord = getExprValue(verNode);

        horCord *= scaleX;
        verCord *= scaleY;

        horTemp = horCord * Math.cos(rotAngle) + verCord * Math.sin(rotAngle);
        verCord = verCord * Math.cos(rotAngle) - horCord * Math.sin(rotAngle);
        horCord = horTemp;

        horCord += originX;
        verCord += originY;

        nowX = horCord;
        nowY = verCord;
    }

    public void drawLoop(double start, double end, double step, ENode horNode, ENode verNode) {
        for (parameter.setValue(start); parameter.getValue() < end; parameter.setValue(parameter.getValue() + step)) {
            calcuCoord(horNode, verNode);
            points.add(new Point((int)nowX, (int)nowY));
            //drawPoint();
        }
        drawPoint();
    }

    private void drawPoint() {
        if (!drawFrame.isLunched) {
            drawFrame.lunchFrame();
        }
        drawFrame.draw(points);
    }

    private static double getReflectValue(String funcName, double data) {
        MyMath myMath = new MyMath();
        Method method;
        try {
            method = myMath.getClass().getDeclaredMethod(funcName, Double.class);
            double result = (double) method.invoke(myMath, data);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public double getOriginX() {
        return originX;
    }

    public void setOriginX(double originX) {
        this.originX = originX;
    }

    public double getOriginY() {
        return originY;
    }

    public void setOriginY(double originY) {
        this.originY = originY;
    }

    public double getScaleX() {
        return scaleX;
    }

    public void setScaleX(double scaleX) {
        this.scaleX = scaleX;
    }

    public double getScaleY() {
        return scaleY;
    }

    public void setScaleY(double scaleY) {
        this.scaleY = scaleY;
    }

    public double getRotAngle() {
        return rotAngle;
    }

    public void setRotAngle(double rotAngle) {
        this.rotAngle = rotAngle;
    }

    public MyDouble getParameter() {
        return parameter;
    }

    public void setParameter(MyDouble parameter) {
        this.parameter = parameter;
    }
}
