package tipos;

public class Sensor {
	private int idSensor;
	private String type;
	private String name;
	private int idDevice;
	private String ubicacion;
	
	
	
	public Sensor() {
		super();
	}



	public Sensor(int idSensor, String type, String name, int idDevice, String ubicacion) {
		super();
		this.idSensor = idSensor;
		this.type = type;
		this.name = name;
		this.idDevice = idDevice;
		this.ubicacion = ubicacion;
	}



	public int getIdSensor() {
		return idSensor;
	}



	public void setIdSensor(int idSensor) {
		this.idSensor = idSensor;
	}



	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public int getIdDevice() {
		return idDevice;
	}



	public void setIdDevice(int idDevice) {
		this.idDevice = idDevice;
	}



	public String getUbicacion() {
		return ubicacion;
	}



	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idDevice;
		result = prime * result + idSensor;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((ubicacion == null) ? 0 : ubicacion.hashCode());
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
		Sensor other = (Sensor) obj;
		if (idDevice != other.idDevice)
			return false;
		if (idSensor != other.idSensor)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (ubicacion == null) {
			if (other.ubicacion != null)
				return false;
		} else if (!ubicacion.equals(other.ubicacion))
			return false;
		return true;
	}



	@Override
	public String toString() {
		return "Sensor [idSensor=" + idSensor + ", type=" + type + ", name=" + name + ", idDevice=" + idDevice
				+ ", ubicacion=" + ubicacion + "]";
	}
	
	
	
	
}
