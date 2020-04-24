package tipos;

public class Sensor_particula_value {
	private Integer idSensor_particula_value;
	private Integer idSensor;
	private Float Particulas_1;
	private Float Particulas_25;
	private Float Particulas_10;
	private Float accuracy;
	private Long timestamp;
	
	
	
	public Sensor_particula_value() {
		super();
	}



	public Sensor_particula_value(Integer idSensor_particula_value, Integer idSensor, Float particulas_1,
			Float particulas_25, Float particulas_10, Float accuracy, Long timestamp) {
		super();
		this.idSensor_particula_value = idSensor_particula_value;
		this.idSensor = idSensor;
		Particulas_1 = particulas_1;
		Particulas_25 = particulas_25;
		Particulas_10 = particulas_10;
		this.accuracy = accuracy;
		this.timestamp = timestamp;
	}



	public Integer getIdSensor_particula_value() {
		return idSensor_particula_value;
	}



	public void setIdSensor_particula_value(Integer idSensor_particula_value) {
		this.idSensor_particula_value = idSensor_particula_value;
	}



	public Integer getIdSensor() {
		return idSensor;
	}



	public void setIdSensor(Integer idSensor) {
		this.idSensor = idSensor;
	}



	public Float getParticulas_1() {
		return Particulas_1;
	}



	public void setParticulas_1(Float particulas_1) {
		Particulas_1 = particulas_1;
	}



	public Float getParticulas_25() {
		return Particulas_25;
	}



	public void setParticulas_25(Float particulas_25) {
		Particulas_25 = particulas_25;
	}



	public Float getParticulas_10() {
		return Particulas_10;
	}



	public void setParticulas_10(Float particulas_10) {
		Particulas_10 = particulas_10;
	}



	public Float getAccuracy() {
		return accuracy;
	}



	public void setAccuracy(Float accuracy) {
		this.accuracy = accuracy;
	}



	public Long getTimestamp() {
		return timestamp;
	}



	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Particulas_1 == null) ? 0 : Particulas_1.hashCode());
		result = prime * result + ((Particulas_10 == null) ? 0 : Particulas_10.hashCode());
		result = prime * result + ((Particulas_25 == null) ? 0 : Particulas_25.hashCode());
		result = prime * result + ((accuracy == null) ? 0 : accuracy.hashCode());
		result = prime * result + ((idSensor == null) ? 0 : idSensor.hashCode());
		result = prime * result + ((idSensor_particula_value == null) ? 0 : idSensor_particula_value.hashCode());
		result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sensor_particula_value other = (Sensor_particula_value) obj;
		if (Particulas_1 == null) {
			if (other.Particulas_1 != null)
				return false;
		} else if (!Particulas_1.equals(other.Particulas_1))
			return false;
		if (Particulas_10 == null) {
			if (other.Particulas_10 != null)
				return false;
		} else if (!Particulas_10.equals(other.Particulas_10))
			return false;
		if (Particulas_25 == null) {
			if (other.Particulas_25 != null)
				return false;
		} else if (!Particulas_25.equals(other.Particulas_25))
			return false;
		if (accuracy == null) {
			if (other.accuracy != null)
				return false;
		} else if (!accuracy.equals(other.accuracy))
			return false;
		if (idSensor == null) {
			if (other.idSensor != null)
				return false;
		} else if (!idSensor.equals(other.idSensor))
			return false;
		if (idSensor_particula_value == null) {
			if (other.idSensor_particula_value != null)
				return false;
		} else if (!idSensor_particula_value.equals(other.idSensor_particula_value))
			return false;
		if (timestamp == null) {
			if (other.timestamp != null)
				return false;
		} else if (!timestamp.equals(other.timestamp))
			return false;
		return true;
	}



	@Override
	public String toString() {
		return "Sensor_particula_value [idSensor_particula_value=" + idSensor_particula_value + ", idSensor=" + idSensor
				+ ", Particulas_1=" + Particulas_1 + ", Particulas_25=" + Particulas_25 + ", Particulas_10="
				+ Particulas_10 + ", accuracy=" + accuracy + ", timestamp=" + timestamp + "]";
	}
	
	
	
	
	
	
}
