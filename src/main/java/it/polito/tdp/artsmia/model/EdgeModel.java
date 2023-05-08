package it.polito.tdp.artsmia.model;

import java.util.Objects;

// Questa classe mi modella l'arco
public class EdgeModel {
	
	private ArtObject source;
	private ArtObject target;
	private Integer weight;
	
	
	public EdgeModel(ArtObject source, ArtObject target, Integer weight) {
		super();
		this.source = source;
		this.target = target;
		this.weight = weight;
	}
	
	public ArtObject getSource() {
		return source;
	}
	public void setSource(ArtObject source) {
		this.source = source;
	}
	
	public ArtObject getTarget() {
		return target;
	}
	public void setTarget(ArtObject target) {
		this.target = target;
	}
	
	public Integer getWeight() {
		return weight;
	}
	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	
	@Override
	public String toString() {
		return "edgeModel [source=" + source + ", target=" + target + ", weight=" + weight + "]";
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(source, target, weight);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EdgeModel other = (EdgeModel) obj;
		return Objects.equals(source, other.source) && Objects.equals(target, other.target)
				&& Objects.equals(weight, other.weight);
	}
	
	
	

}
