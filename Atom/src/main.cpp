#include <Wire.h>
#include <SPI.h>
#include <Arduino.h>
#define ARDUINOJSON_USE_LONG_LONG 1
#include "ArduinoJson.h"
#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <Adafruit_Sensor.h>


#include <DHT.h>

#include <SoftwareSerial.h>

#if defined(ESP8266) && !defined(D5)
#define D5 (14)
#define D6 (12)
#define D7 (13)
#define D8 (15)
#endif


char responseBuffer[300];
WiFiClient client;

String SSID="JM63";
String PASS="@j63mr655jtAEFpFN7kb";

String SERVER_IP="192.168.1.133";
int SERVER_PORT= 8082;


//utilizamos el parametro dado para introducir el timestand y ademas la Alarma
//para tendra otro para cambiar su valor
int sendGetActuadorAlarmaValue();
void sendPostActuadorAlarmaValue(int, int);

//utilizamos el parametro dado para introducir el timestand
void sendGetSensorParticulaValue();
void sendPostSensorParticulaValue(int);

void sendGetSensorDHT11Value();
void sendPostSensorDHT11Value(int);


//definicion de pines y estructuras//////////////////////////////

int timeNow=0;

#define DHTTYPE    DHT11
#define DHTPIN D5


DHT dht(DHTPIN, DHTTYPE);

SoftwareSerial pmsSerial(D7, D8);
struct pms5003data {
uint16_t framelen;
uint16_t pm10_standard, pm25_standard, pm100_standard;
uint16_t pm10_env, pm25_env, pm100_env;
uint16_t particles_03um, particles_05um, particles_10um, particles_25um, particles_50um,
particles_100um;
uint16_t unused;
uint16_t checksum;
};
struct pms5003data data;
boolean readPMSdata(Stream *);



/////////setup///////////////////////
void setup() {

  ////Inicializacion de conexion
  Serial.begin(115200);
  pmsSerial.begin(9600);//Puerto serie detector particulas_1
  //Pines para las luces
  pinMode(D2, OUTPUT);
  pinMode(D3, OUTPUT);
  pinMode(D4, OUTPUT);

  WiFi.begin(SSID, PASS);
  Serial.print("Connecting...");
  while (WiFi.status() != WL_CONNECTED){
    delay(500);
    Serial.print(".");
  }
  Serial.print("Connected, IP address: ");
  Serial.print(WiFi.localIP());


////Inicializacion de dht11
  dht.begin();


}



void loop() {
  float value=0.0;
    value= sendGetActuadorAlarmaValue();
    Serial.print(value);
    if(value==1.0){
    //El led sera naranja si los niveles son intermedios
    digitalWrite(D2, LOW);
    digitalWrite(D4, HIGH);
    digitalWrite(D3, HIGH);


    }else if(value==2.0){
      //El led sera rojo si los niveles son elevados
      digitalWrite(D2, LOW);
      digitalWrite(D4, HIGH);
      digitalWrite(D3, LOW);
    }else{
      //El led sera verde si los niveles son buenos
      digitalWrite(D2, LOW);
      digitalWrite(D4, LOW);
      digitalWrite(D3, HIGH);
    }

float h = dht.readHumidity();
float t = dht.readTemperature();


if (readPMSdata(&pmsSerial)) {
// reading data was successful!

if(data.pm10_env>150||data.pm25_env>150||data.pm100_env>150){

  sendPostActuadorAlarmaValue(2,timeNow);

}else if((data.pm10_env>50)&(data.pm10_env<150)||(data.pm25_env>50)&(data.pm25_env<150)||(data.pm100_env>50)&(data.pm100_env<150)){
  sendPostActuadorAlarmaValue(1,timeNow);
}else{
  sendPostActuadorAlarmaValue(0,timeNow);
}

//Enviamos los valores a las tablas
sendPostSensorParticulaValue(timeNow);
Serial.println("---------------------------------------");
Serial.println("---------------------------------------");
sendPostSensorDHT11Value(timeNow);

timeNow= timeNow+1;
Serial.println("------------------HOLAAA---------------------");

Serial.println();
Serial.println("---------------------------------------");
Serial.println("Concentration Units (environmental)");
Serial.print("PM 1.0: "); Serial.print(data.pm10_env);
Serial.print("\t\tPM 2.5: "); Serial.print(data.pm25_env);
Serial.print("\t\tPM 10: "); Serial.println(data.pm100_env);
Serial.println("---------------------------------------");
Serial.print("Humidity: ");    // show in serial monitor
Serial.print(h);
Serial.print(" %\t");
Serial.print("Temperature: ");    // show in serial monitor
Serial.print(t);
Serial.print(" *C \n");
Serial.print(value);
delay(2000);
}

}



//Funcion para leer el puerto serie
boolean readPMSdata(Stream *s) {
if (! s->available()) {
Serial.println("avaliable");
return false;

}
// Read a byte at a time until we get to the special '0x42' start-byte
if (s->peek() != 0x42) {

s->read();
return false;
}
// Now read all 32 bytes
if (s->available() < 32) {

return false;
}
uint8_t buffer[32];
uint16_t sum = 0;
s->readBytes(buffer, 32);
// get checksum ready
for (uint8_t i=0; i<30; i++) {
sum += buffer[i];
}

// The data comes in endian'd, this solves it so it works on all platforms
uint16_t buffer_u16[15];
for (uint8_t i=0; i<15; i++) {
buffer_u16[i] = buffer[2 + i*2 + 1];
buffer_u16[i] += (buffer[2 + i*2] << 8);
}
// put it into a nice struct :)
memcpy((void *)&data, (void *)buffer_u16, 30);
if (sum != data.checksum) {
Serial.println("Checksum failure");
return false;
}
// success!
return true;
}



//--------------------------------------------------------------




int sendGetActuadorAlarmaValue(){
  if (WiFi.status() == WL_CONNECTED){
    HTTPClient http;
    http.begin(client, SERVER_IP, SERVER_PORT, "/api/actuador/actuadorAlarmaValue/1", true);
    int httpCode = http.GET();

    Serial.println("Response code: " + httpCode);

    String payload = http.getString();
Serial.println(payload);
    const size_t capacity = JSON_OBJECT_SIZE(5) + JSON_ARRAY_SIZE(5) + 1000;
    DynamicJsonDocument doc(capacity);

    DeserializationError error = deserializeJson(doc, payload);

    if (error){
      Serial.print("deserializeJson() failed: ");
      Serial.println(error.c_str());

    }



    Serial.println(F("Response:"));
    int idActuador_alarma_value = doc[0][("idActuador_alarma_value")].as<int>();
    int idActuador = doc[0][("idActuador")].as<int>();
    float value = doc[0]["value"].as<float>();
    long timestamp = doc[0]["timestamp"].as<long>();

    Serial.println("idActuador_alarma_value: " + String(idActuador_alarma_value));
    Serial.println("idActuador: " + String(idActuador));
    Serial.println("Valor: " + String(value));
    Serial.println("timestamp: " + String(timestamp));
    return value;
  }

}
void sendPostActuadorAlarmaValue(int i,int timeNow){
    if (WiFi.status() == WL_CONNECTED){
      HTTPClient http;
      http.begin(client, SERVER_IP, SERVER_PORT, "/api/actuador/actuadorAlarmaValue", true);
      http.addHeader("Content-Type", "application/json");

      const size_t capacity = JSON_OBJECT_SIZE(5) + JSON_ARRAY_SIZE(5) + 1000;
      DynamicJsonDocument doc(capacity);
      doc["idActuador"]=1;
      doc["value"]=i;
      doc["timestamp"]=timeNow;


      String output;
      serializeJson(doc, output);

      int httpCode = http.PUT(output);

      Serial.println("Response code: " + httpCode);

      String payload = http.getString();

      Serial.println("Resultado: " + payload);
  }
}




//-----------------------------------------------------------------------------





void sendGetSensorParticulaValue(){
  if (WiFi.status() == WL_CONNECTED){
    HTTPClient http;
    http.begin(client, SERVER_IP, SERVER_PORT, "/api/sensor/particulas_values/2", true);
    int httpCode = http.GET();

    Serial.println("Response code: " + httpCode);

    String payload = http.getString();

    const size_t capacity = JSON_OBJECT_SIZE(5) + JSON_ARRAY_SIZE(5) + 1000;
    DynamicJsonDocument doc(capacity);

    DeserializationError error = deserializeJson(doc, payload);

    if (error){
      Serial.print("deserializeJson() failed: ");
      Serial.println(error.c_str());
      return;
    }



    Serial.println(F("Response:"));
    int idSensor_particula_value = doc[0][("idSensor_particula_value")].as<int>();
    int idSensor = doc[0][("idSensor")].as<int>();
    float Particulas_1 = doc[0]["particulas_1"].as<float>();
    float Particulas_25 = doc[0]["particulas_25"].as<float>();
    float Particulas_10 = doc[0]["particulas_10"].as<float>();
    float accuracy = doc[0]["accuracy"].as<float>();
    long timestamp = doc[0]["timestamp"].as<long>();

    Serial.println("idSensor_particula_value: " + String(idSensor_particula_value));
    Serial.println("idSensor: " + String(idSensor));
    Serial.println("Particulas_1: " + String(Particulas_1));
    Serial.println("Particulas_25: " + String(Particulas_25));
    Serial.println("Particulas_10: " + String(Particulas_10));
    Serial.println("accuracy: " + String(accuracy));
    Serial.println("timestamp: " + String(timestamp));

  }
}


  void sendPostSensorParticulaValue(int timeNow){
    if (WiFi.status() == WL_CONNECTED){
      HTTPClient http;
      http.begin(client, SERVER_IP, SERVER_PORT, "/api/sensor/particulas_values", true);
      http.addHeader("Content-Type", "application/json");

      const size_t capacity = JSON_OBJECT_SIZE(5) + JSON_ARRAY_SIZE(5) + 1000;
      DynamicJsonDocument doc(capacity);
      doc["idSensor"]=2;
      doc["accuracy"]=19.0;
      doc["timestamp"]=timeNow;
      doc["particulas_1"]=data.pm10_env;
      doc["particulas_25"]=data.pm25_env;
      doc["particulas_10"]=data.pm100_env;



      String output;

      serializeJson(doc, output);


      int httpCode = http.PUT(output);

      Serial.println("Response code: " + httpCode);

      String payload = http.getString();

      Serial.println("Resultado: " + payload);
  }
}

//-----------------------------------------------------------------------------


void sendGetSensorDHT11Value(){
  if (WiFi.status() == WL_CONNECTED){
    HTTPClient http;
    http.begin(client, SERVER_IP, SERVER_PORT, "/api/sensor/DHT11_values/1", true);
    int httpCode = http.GET();

    Serial.println("Response code: " + httpCode);

    String payload = http.getString();

    const size_t capacity = JSON_OBJECT_SIZE(5) + JSON_ARRAY_SIZE(5) + 1000;
    DynamicJsonDocument doc(capacity);

    DeserializationError error = deserializeJson(doc, payload);

    if (error){
      Serial.print("deserializeJson() failed: ");
      Serial.println(error.c_str());
      return;
    }



    Serial.println(F("Response:"));
    int idSensor_DHT11_value = doc[0][("idSensor_DHT11_value")].as<int>();
    int idSensor = doc[0][("idSensor")].as<int>();
    float Temperatura = doc[0]["temperatura"].as<float>();
    float Humedad = doc[0]["humedad"].as<float>();
    float accuracy = doc[0]["accuracy"].as<float>();
    long timestamp = doc[0]["timestamp"].as<long>();

    Serial.println("idSensor_DHT11_value: " + String(idSensor_DHT11_value));
    Serial.println("idSensor: " + String(idSensor));
    Serial.println("Temperatura: " + String(Temperatura));
    Serial.println("Humedad: " + String(Humedad));
    Serial.println("accuracy: " + String(accuracy));
    Serial.println("timestamp: " + String(timestamp));

  }
}
  void sendPostSensorDHT11Value(int timeNow){
    if (WiFi.status() == WL_CONNECTED){
      HTTPClient http;
      http.begin(client, SERVER_IP, SERVER_PORT, "/api/sensor/DHT11_values", true);
      http.addHeader("Content-Type", "application/json");

      const size_t capacity = JSON_OBJECT_SIZE(5) + JSON_ARRAY_SIZE(5) + 1000;
      DynamicJsonDocument doc(capacity);
      doc[("idSensor")]=1;
      doc["temperatura"]=dht.readTemperature();
      doc["humedad"]=dht.readHumidity();;
      doc["accuracy"]=19;
      doc["timestamp"]=timeNow;


      String output;
      serializeJson(doc, output);
      Serial.print(output);
      int httpCode = http.PUT(output);

      Serial.println("Response code: " + httpCode);

      String payload = http.getString();

      Serial.println("Resultado: " + payload);
  }
}
