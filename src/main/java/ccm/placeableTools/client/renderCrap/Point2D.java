package ccm.placeableTools.client.renderCrap;

public class Point2D
{
    double U, V;

    public Point2D(double U, double V)
    {
        this.U = U;
        this.V = V;
    }

    public double getU()
    {
        return U;
    }

    public void setU(double u)
    {
        this.U = u;
    }

    public double getV()
    {
        return V;
    }

    public void setV(double v)
    {
        this.V = v;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point2D point2D = (Point2D) o;

        if (Double.compare(point2D.U, U) != 0) return false;
        if (Double.compare(point2D.V, V) != 0) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result;
        long temp;
        temp = Double.doubleToLongBits(U);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(V);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }


}
