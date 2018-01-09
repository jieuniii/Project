package main

import (
	"fmt"
	"os/exec"
	"log"
	_ "github.com/go-sql-driver/mysql"
	"database/sql"
)

func temp() {
	
	cmd := "ver"
	fmt.Println("command: ", cmd)
	
	out, err := exec.Command("cmd","/C",cmd).Output()
	if err != nil {
		log.Fatal(err)
	}
	
	fmt.Println("result: ", string(out))

}

func db() {
	 db, err := sql.Open("mysql", "")
       if err != nil {
               log.Fatal(err)
       }
       defer db.Close()

       var term string

       rows, err := db.Query("SELECT pos FROM CoinedWord")
       if err != nil {
                log.Fatal(err)
       }
       
       defer rows.Close() //반드시 닫아야 함

       for rows.Next() {
               err := rows.Scan(&term)
               
               if err != nil {
               log.Fatal(err)
               }
               
               fmt.Println(term)

       }
}

func main() {
	
	db()
	temp()
	
}