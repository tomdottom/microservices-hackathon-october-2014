package main

import (
	"encoding/json"
	"fmt"
	"log"
	"time"

	"gopkg.in/mgo.v2"
	// "gopkg.in/mgo.v2/bson"
	// "github.com/gin-gonic/gin"
	"github.com/streadway/amqp"
)

var baseUrl = "http://combo-squirrel.herokuapp.com/"

type Message struct {
	Id       string  `json:"id"`
	Name     string  `json:"name"`
	Location Vector  `json:"location"`
	Velocity Vector  `json:"velocity"`
	Vector   Vector  `json:"vector"`
	Colour   Colour  `json:"colour"`
	Radius   float64 `json:"radius"`
	Mass     float64 `json:"mass"`
	Force    Vector  `json:"force"`
}

type Message2 struct {
	Id       int       `json:"id"`
	Name     string    `json:"name"`
	Location []float64 `json:"location"`
	Velocity []float64 `json:"velocity"`
	Vector   []float64 `json:"vector"`
	Colour   Colour    `json:"colour"`
	Radius   float64   `json:"radius"`
	Mass     float64   `json:"mass"`
	Force    []float64 `json:"force"`
}

type Colour struct {
	Red   int `json:"R"`
	Green int `json:"G"`
	Blue  int `json:"B"`
}

type Vector struct {
	X float64
	Y float64
}

type Planet struct {
	Id       string `json:"id"`
	Name     string `json:"name"`
	Colour   Colour
	Location Vector
	Vector   Vector
	Radius   float64 `json:"radius"`
	Mass     float64 `json:"mass"`
}

func main() {
	go func() {
		api := API{"8042", nil}
		api.Start()
	}()

	fmt.Println(time.Now(), " - Server Starts")

	mongoSession, err := mgo.Dial("localhost:4242")
	if err != nil {
		panic(err)
	}
	defer mongoSession.Close()

	// Optional. Switch the session to a monotonic behavior.
	mongoSession.SetMode(mgo.Monotonic, true)
	messageCo := mongoSession.DB("planetside").C("messages")

	source, err := amqp.Dial("amqp://178.62.106.39:5672/")
	if err != nil {
		log.Fatalf("connection.open source: %s", err)
	}
	defer source.Close()

	chs, err := source.Channel()
	if err != nil {
		log.Fatalf("channel.open source: %s", err)
	}

	// QueueDeclare(name, durable, delete when unused, exclusive, no-wait, args)
	q, err := chs.QueueDeclare("world", false, false, false, false, nil)

	// m := &Message{"Record", "I'm connected"}
	// mBody, _ := json.Marshal(m)

	// // Publish(exchange, key, mandatory, immediate, msg)
	// chs.Publish("combo", q.Name, false, false, amqp.Publishing{ContentType: "text/plain", Body: mBody})
	// QueueBind(queue.Name, key, exchange, noWait, args)
	err = chs.QueueBind(q.Name, "#", "combo", false, nil)

	// p := &Planet{"1234567", "Uranus", Colour{255, 0, 0}, Vector{0.5, 10.5}, Vector{0.5, 0.2}, 0.7, 10.0}
	// mBody, _ = json.Marshal(p)
	// chs.Publish("combo", q.Name, false, false, amqp.Publishing{ContentType: "text/plain", Body: mBody})

	// Consume(queue.Name, consumer, , , , ,)
	msgs, err := chs.Consume(q.Name, "", true, false, false, false, nil)

	forever := make(chan bool)

	go func() {
		for d := range msgs {
			log.Printf("Received a message: %s", d.Body)
			m := Message{}
			if err := json.Unmarshal(d.Body, &m); err == nil {
				fmt.Println("Message: ", m)
				messageCo.Insert(&m)

			} else {
				log.Printf("Can't unmarshall message: ", err)
				m2 := Message2{}
				if err2 := json.Unmarshal(d.Body, &m2); err2 == nil {
					fmt.Println("Message: ", m2)
					messageCo.Insert(&m2)
				} else {
					log.Printf("Can't unmarshall message2: ", err2)
				}
			}
		}
	}()

	<-forever
	fmt.Println(time.Now(), " - Server Stops")
}
