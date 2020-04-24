package tipos;

public class Actuador {
	
	private Integer idActuador;
	private String type;
	private String name;
	private Integer idDevice;
	private String ubicacion;
	
	public Actuador() {
		super();
	}

	public Actuador(Integer idActuador, String type, String name, Integer idDevice, String ubicacion) {
		super();
		this.idActuador = idActuador;
		this.type = type;
		this.name = name;
		this.idDevice = idDevice;
		this.ubicacion = ubicacion;
	}

	public Integer getIdActuador() {
		return idActuador;
	}

	public void setIdActuador(Integer idActuador) {
		this.idActuador = idActuador;
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

	public Integer getIdDevice() {
		return idDevice;
	}

	public void setIdDevice(Integer idDevice) {
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
		result = prime * result + ((idActuador == null) ? 0 : idActuador.hashCode());
		result = prime * result + ((idDevice == null) ? 0 : idDevice.hashCode());
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
		Actuador other = (Actuador) obj;
		if (idActuador == null) {
			if (other.idActuador != null)
				return false;
		} else if (!idActuador.equals(other.idActuador))
			return false;
		if (idDevice == null) {
			if (other.idDevice != null)
				return false;
		} else if (!idDevice.equals(other.idDevice))
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
		return "Actuador [idActuador=" + idActuador + ", type=" + type + ", name=" + name + ", idDevice=" + idDevice
				+ ", ubicacion=" + ubicacion + "]";
	}
	
	
	
	
}
