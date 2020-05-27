package tipos;

public class Sensor_dht11_value {
	private int idSensor_dht11_value;
	private int idSensor;
	private float Temperatura;
	private float Humedad;
	private float accuracy;
	private long timestamp;
	
	public Sensor_dht11_value() {
		super();
	}

	public Sensor_dht11_value(int idSensor_dht11_value, int idSensor, float temperatura, float humedad, float accuracy,
			long timestamp) {
		super();
		this.idSensor_dht11_value = idSensor_dht11_value;
		this.idSensor = idSensor;
		Temperatura = temperatura;
		Humedad = humedad;
		this.accuracy = accuracy;
		this.timestamp = timestamp;
	}

	public int getIdSensor_dht11_value() {
		return idSensor_dht11_value;
	}

	public void setIdSensor_dht11_value(int idSensor_dht11_value) {
		this.idSensor_dht11_value = idSensor_dht11_value;
	}

	public int getIdSensor() {
		return idSensor;
	}

	public void setIdSensor(int idSensor) {
		this.idSensor = idSensor;
	}

	public float getTemperatura() {
		return Temperatura;
	}

	public void setTemperatura(float temperatura) {
		Temperatura = temperatura;
	}

	public float getHumedad() {
		return Humedad;
	}

	public void setHumedad(float humedad) {
		Humedad = humedad;
	}

	public float getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(float accuracy) {
		this.accuracy = accuracy;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(Humedad);
		result = prime * result + Float.floatToIntBits(Temperatura);
		result = prime * result + Float.floatToIntBits(accuracy);
		result = prime * result + idSensor;
		result = prime * result + idSensor_dht11_value;
		result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
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
		Sensor_dht11_value other = (Sensor_dht11_value) obj;
		if (Float.floatToIntBits(Humedad) != Float.floatToIntBits(other.Humedad))
			return false;
		if (Float.floatToIntBits(Temperatura) != Float.floatToIntBits(other.Temperatura))
			return false;
		if (Float.floatToIntBits(accuracy) != Float.floatToIntBits(other.accuracy))
			return false;
		if (idSensor != other.idSensor)
			return false;
		if (idSensor_dht11_value != other.idSensor_dht11_value)
			return false;
		if (timestamp != other.timestamp)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Sensor_dht11_value [idSensor_dht11_value=" + idSensor_dht11_value + ", idSensor=" + idSensor
				+ ", Temperatura=" + Temperatura + ", Humedad=" + Humedad + ", accuracy=" + accuracy + ", timestamp="
				+ timestamp + "]";
	}
	
	
	
}
