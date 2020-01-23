package utils;
import java.util.Collection;
import dataStructure.node_data;

/**
 * This class is for scaling calculation purposes.
 * @author Daniel Korotine & Yahav Karpel
 *
 */

public class Scaling {
	
	/**
	 * this function scales values according to given ranges.
	 * @param data denote some data to be scaled
	 * @param r_min the minimum of the range of your data
	 * @param r_max the maximum of the range of your data
	 * @param t_min the minimum of the range of your desired target scaling
	 * @param t_max the maximum of the range of your desired target scaling
	 * @return
	 */
	
	public static double scale(double data, double r_min, double r_max, double t_min, double t_max) {
		double res = ((data - r_min) / (r_max-r_min)) * (t_max - t_min) + t_min;
		return res;
	}

	/**
	 * get minimum x value of all nodes in graph
	 * @param nodes list of nodes in graph
	 * @return	min x value
	 */
	
	public static double getMinX(Collection<node_data> nodes) {
		double min = Double.MAX_VALUE;

		for(node_data node : nodes) {
			double temp = node.getLocation().x();
			if(temp<min)
				min=temp;
		}
		return min;
	}
	
	/**
	 * get minimum y value of all nodes in graph
	 * @param nodes list of nodes in graph
	 * @return	min y value
	 */
	
	public static double getMinY(Collection<node_data> nodes) {
		double min = Double.MAX_VALUE;

		for(node_data node : nodes) {
			double temp = node.getLocation().y();
			if(temp<min)
				min=temp;
		}
		return min;
	}
	
	/**
	 * get maximum x value of all nodes in graph
	 * @param nodes list of nodes in graph
	 * @return	max x value
	 */
	
	public static double getMaxX(Collection<node_data> nodes) {
		double max = Double.MIN_VALUE;

		for(node_data node : nodes) {
			double temp = node.getLocation().x();
			if(temp>max)
				max=temp;
		}
		return max;
	}
	
	/**
	 * get maximum y value of all nodes in graph
	 * @param nodes list of nodes in graph
	 * @return	max y value
	 */
	
	public static double getMaxY(Collection<node_data> nodes) {
		double max = Double.MIN_VALUE;

		for(node_data node : nodes) {
			double temp = node.getLocation().y();
			if(temp>max)
				max=temp;
		}
		return max;
	}
}
