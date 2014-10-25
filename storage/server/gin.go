package main

import (
	"github.com/gin-gonic/gin"
	"gopkg.in/mgo.v2"
	"gopkg.in/mgo.v2/bson"
	"strconv"
)

type API struct {
	Port string
	DB   *mgo.Database
}

func (a *API) Start() {
	session, err := mgo.Dial("localhost:4242")
	if err != nil {
		panic(err)
	}
	defer session.Close()
	a.DB = session.DB("planetside")
	r := gin.Default()

	r.GET("/planets", func(c *gin.Context) {
		c.Writer.Header().Set("Access-Control-Allow-Origin", "*")
		var result []map[string]interface{}
		a.DB.C("planets").Find(nil).All(&result)
		c.JSON(200, result)
	})

	r.GET("/counters", func(c *gin.Context) {
		c.Writer.Header().Set("Access-Control-Allow-Origin", "*")
		planetCount, _ := a.DB.C("planets").Count()
		destroyedCount, _ := a.DB.C("messages").Find(bson.M{"type": "body.destroy"}).Count()
		messageCount, _ := a.DB.C("messages").Count()
		var result map[string]interface{}
		a.DB.C("events").Find(bson.M{"type": "time"}).Sort("-$natural").One(&result)
		timeCount, _ := strconv.Atoi(result["content"].(string))

		c.JSON(200, gin.H{"planets": planetCount, "timeCount": timeCount, "messages": messageCount, "destroyed": destroyedCount})
	})
	r.Run(":" + a.Port)
}
