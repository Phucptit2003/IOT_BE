#include <WiFi.h>
#include <WebSocketsServer.h>
#include <Adafruit_Sensor.h>
#include <DHT.h>
#include <ArduinoJson.h>

#define DHTPIN 21
#define DHTTYPE DHT11
#define FAN_PIN 25
#define LED_PIN 26
#define BUZZER_PIN 27

DHT dht(DHTPIN, DHTTYPE);
WebSocketsServer webSocket = WebSocketsServer(81);

const char* ssid = "Phuc";
const char* password = "88888888";

// Biến để lưu trữ trạng thái thiết bị
int fanSpeed = 0;
int targetTemperature = 35; // Nhiệt độ mục tiêu cao
int targetHumidity = 75;    // Độ ẩm mục tiêu cao
int mediumTemperature = 30; // Mức nhiệt độ trung bình
int mediumHumidity = 60;    // Mức độ ẩm trung bình
bool ledState = false;
bool buzzerState = false;
bool dhtSensorError = false;    // Trạng thái lỗi của cảm biến DHT
bool otherSensorError = false;  // Trạng thái lỗi của các cảm biến khác
bool isBuzzerOn = false;
bool isLedOn = false;
bool isDefault = true;
int fanSpeedChange =0;
void setup() {
  Serial.begin(9600);
  dht.begin();
  pinMode(FAN_PIN, OUTPUT);
  pinMode(LED_PIN, OUTPUT);
  pinMode(BUZZER_PIN, OUTPUT);
  // Loa tắt
    digitalWrite(BUZZER_PIN, LOW); 
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("Connected!");

  Serial.print("IP Address: ");
  Serial.println(WiFi.localIP());

  webSocket.begin();
  webSocket.onEvent(webSocketEvent);
}

void loop() {
  webSocket.loop();
  Serial.print("IP Address: ");
  Serial.println(WiFi.localIP());
  Serial.print("===========================================");
  digitalWrite(BUZZER_PIN, LOW);
  float humidity = dht.readHumidity();
  float temperature = dht.readTemperature();

  // Kiểm tra lỗi cảm biến DHT
  if (isnan(humidity) || isnan(temperature)) {
    dhtSensorError = true;
    Serial.println("DHT Sensor error: Unable to read data from DHT sensor!");
    sendSensorError("DHT");
    stopAllSensors();
  } else {
    dhtSensorError = false;
    // Kiểm tra và điều chỉnh theo ngưỡng nhiệt độ và độ ẩm
    controlEnvironment(temperature, humidity);
  }

  delay(2000);
}

void controlEnvironment(float temperature, float humidity) {
  Serial.printf("isDefault Controll: %d\n", isDefault);
  // Mức điều khiển dựa trên ngưỡng nhiệt độ và độ ẩm
  if (humidity > targetHumidity || temperature > targetTemperature) {  // Mức cao
    // Quạt chạy tối đa
    if(isDefault == true){
       fanSpeed = 255;
      ledState = true;
       digitalWrite(BUZZER_PIN, HIGH);
    }
    else{
      fanSpeed =fanSpeedChange;
    if(isLedOn ==true)
      ledState = true; // Đèn bật
      else ledState = false;
      if(isBuzzerOn== true)
      digitalWrite(BUZZER_PIN, HIGH);
      else digitalWrite(BUZZER_PIN, LOW);
    }
    Serial.println("High level: Fan at max, LED ON, Buzzer ON");
  } else if (humidity > mediumHumidity || temperature > mediumTemperature) {  // Mức trung bình
     // Quạt chạy chậm hơn
    if(isDefault == true){
      fanSpeed = 128;
      ledState = true;
       digitalWrite(BUZZER_PIN, LOW);
    }
    else{
      fanSpeed = fanSpeedChange;
      if(isLedOn ==true)
      ledState = true; // Đèn bật
      else ledState = false;
      if(isBuzzerOn ==false)
      digitalWrite(BUZZER_PIN, LOW);
      else digitalWrite(BUZZER_PIN, HIGH);
      Serial.println("Medium level: Fan at medium, LED ON, Buzzer OFF");
    }
  } else {  // Mức bình thường
    if(isDefault == true){
      fanSpeed = 0;
      ledState = false;
       digitalWrite(BUZZER_PIN, LOW);
    }
    else{
      fanSpeed = fanSpeedChange;
      if( isLedOn == false)
      ledState = false; // Đèn tắt
      else ledState =true;
      if(isBuzzerOn ==false)
      digitalWrite(BUZZER_PIN, LOW);
      else digitalWrite(BUZZER_PIN, HIGH);
      Serial.println("Normal level: Fan OFF, LED OFF, Buzzer OFF");
    }
  }

  // Cập nhật trạng thái quạt, đèn và loa
  analogWrite(FAN_PIN, fanSpeed);
  digitalWrite(LED_PIN, ledState ? HIGH : LOW);
  //digitalWrite(BUZZER_PIN, buzzerState ? HIGH : LOW);

  // Gửi dữ liệu cảm biến qua WebSocket
  sendSensorData(temperature, humidity);
}

void sendSensorData(float temperature, float humidity) {
  // Tạo phản hồi JSON
  String json = "{\"temperature\":" + String(temperature) +
                 ",\"humidity\":" + String(humidity) +
                 ",\"fanSpeed\":" + String(fanSpeed) +
                 ",\"ledState\":\"" + String(ledState ? "on" : "off") + "\"" +
                 ",\"buzzerState\":\"" + String(buzzerState ? "on" : "off") + "\"" +
                 ",\"targetTemperature\":" + String(targetTemperature) +
                 ",\"targetHumidity\":" + String(targetHumidity) +
                 ",\"mediumTemperature\":" + String(mediumTemperature) +
                 ",\"mediumHumidity\":" + String(mediumHumidity) + "}";
  
  webSocket.broadcastTXT(json);
}

void sendSensorError(String sensorName) {
  // Tạo thông báo lỗi cảm biến và gửi qua WebSocket
  String errorJson = "{\"error\":\"" + sensorName + " sensor not responding!\"}";
  webSocket.broadcastTXT(errorJson);
}

void stopAllSensors() {
  // Tắt tất cả các thiết bị khi cảm biến DHT bị lỗi
  analogWrite(FAN_PIN, 0);
  digitalWrite(LED_PIN, LOW);
  digitalWrite(BUZZER_PIN, LOW);
  fanSpeed = 0;
  ledState = false;
  buzzerState = false;
  Serial.println("All sensors stopped due to DHT sensor failure.");
}

void webSocketEvent(uint8_t num, WStype_t type, uint8_t * payload, size_t length) {
  switch (type) {
    case WStype_DISCONNECTED:
      Serial.printf("[%u] Disconnected!\n", num);
      break;
    case WStype_CONNECTED:
      {
        IPAddress ip = webSocket.remoteIP(num);
        Serial.printf("[%u] Connected from %d.%d.%d.%d\n", num, ip[0], ip[1], ip[2], ip[3]);
      }
      break;
    case WStype_TEXT:
      Serial.printf("[%u] Text: %s\n", num, payload);
      handleWebSocketMessage(String((char*)payload)); // Xử lý tin nhắn từ WebSocket
      break;
    case WStype_BIN:
      Serial.printf("[%u] Binary: %u bytes\n", num, length);
      break;
  }
}

void handleWebSocketMessage(String message) {
  // Phân tích cú pháp tin nhắn JSON
  DynamicJsonDocument doc(1024);
  DeserializationError error = deserializeJson(doc, message);
  if (error) {
    Serial.println("Error parsing JSON");
    return;
  }
  Serial.println("DANHAN=======================");
  Serial.printf("isDefault: %d\n", isDefault);
  // Cập nhật giá trị từ client
  if (doc.containsKey("targetTemperature")) {
    targetTemperature = doc["targetTemperature"];
    
    Serial.printf("Target Temperature set to: %d\n", targetTemperature);
  }

  if (doc.containsKey("targetHumidity")) {
    targetHumidity = doc["targetHumidity"];
    Serial.printf("Target Humidity set to: %d\n", targetHumidity);
  }

  if (doc.containsKey("mediumTemperature")) {
    mediumTemperature = doc["mediumTemperature"];
    Serial.printf("Medium Temperature set to: %d\n", mediumTemperature);
  }

  if (doc.containsKey("mediumHumidity")) {
    mediumHumidity = doc["mediumHumidity"];
    Serial.printf("Medium Humidity set to: %d\n", mediumHumidity);
  }

  if (doc.containsKey("fanSpeed")) {
    fanSpeed = doc["fanSpeed"];
    fanSpeedChange = fanSpeed;
    analogWrite(FAN_PIN, fanSpeedChange);
    Serial.printf("Fan Speed set to: %d\n", fanSpeed);
  }

  if (doc.containsKey("ledState")) {
    if(doc["ledState"]=="default"){
      isDefault = true;
    }
    else{
      isDefault= false;
      ledState = doc["ledState"] == "on";
      if(ledState == true) isLedOn = true;
      else isLedOn = false;
      Serial.printf("LED State set to: %s\n", ledState ? "on" : "off");
    }
  }

  if (doc.containsKey("buzzerState")) {
    if(doc["buzzerState"] =="default"){
      isDefault =true;
    }else{
      isDefault =false;
      buzzerState = doc["buzzerState"]=="on";
      if (buzzerState == true){
        isBuzzerOn =true;
      }
      else isBuzzerOn = false;
    }
    
    Serial.printf("Buzzer State set to: %s\n", buzzerState ? "on" : "off");
  }
}

void checkAllDevices() {
  Serial.println("Checking all devices...");

  // Activate LED
  digitalWrite(LED_PIN, HIGH); // Turn on the LED
  Serial.println("LED is ON");

  // Activate Buzzer
  digitalWrite(BUZZER_PIN, HIGH); // Turn on the Buzzer
  Serial.println("Buzzer is ON");

  // Activate Fan
  analogWrite(FAN_PIN, 255); // Set Fan to max speed
  Serial.println("Fan is ON at max speed");

  // Wait for a moment to observe the devices' states
  delay(2000); // Keep them on for 2 seconds

  // Turn off devices after checking
  digitalWrite(LED_PIN, LOW); // Turn off the LED
  digitalWrite(BUZZER_PIN, LOW); // Turn off the Buzzer
  analogWrite(FAN_PIN, 0); // Turn off the Fan

  Serial.println("All devices turned OFF after checking.");
}
