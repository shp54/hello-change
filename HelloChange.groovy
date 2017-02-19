
class Register {

	//Map of bill value -> number of bills currently in register
	def cash = [
		(20): 0,
		(10): 0,
		(5): 0,
		(2): 0,
		(1): 0,
	]
	
	//Given a map of bill values -> number of bills, adds them to what's currently in the register
	def put(def cashMap){
		cashMap.each { k, v -> 
			cash[k] += cashMap[k]
		}
	}
	
	//Given a map of bill values -> number of bills, subtracts them from what's currently in the register
	def take(def cashMap){
		cashMap.each { k, v -> 
			cash[k] -= cashMap[k]
		}
	}
	
	//Gets the total value given a map of bill values and number of bills
	//Uses separate method to calculate total because we also calculate total when getting change
	Integer getTotal(){
		calcTotal(cash)
	}
	
	String toString(){
		"\$${getTotal()} ${cash.values().join(' ')}"
	}
	
	def calcTotal(def cashMap){
		//Returns the sum of each key * each value by recursively calling the closure against the map
		//Advantage of this approach is that you can remove or add keys from the map without breaking anything
		cashMap.inject(0){ total, denomination, bills -> total += denomination * bills } 
	}
	
	//Details for change method
	//This is based on the naive human way of making change - see if you have enough $5s, then enough $2s, then enough $1s
	//Returns map of bills -> number of bills, and returns an empty map if unsuccessful
	//Return value of this method can be passed to take
	def getChange(def amount){
		def change = [:]
		def amountToGet = amount //A copy of amount that we can mutate when we pass through the loop
		def denominations = cash.keySet().findAll { it <= amount }.sort() //Sort denominations in descending order so you can get the max by popping items off the list
		
		//Bail early if amount is more than what's currently in the register
		if(amount > getTotal()){
			println "Sorry, I don't have change for \$$amount"
			return [:]
		}
		
		while(!denominations.empty){ //Check every kind of bill that could make change
			def totalChange = calcTotal(change)
			if(totalChange == amount){ //Exit loop if we have enough change already
				return change
			}
		
			def bill = denominations.pop() //Get largest unexamined bill 
			def billsToGet
			if((bill * cash[bill]) >= amountToGet){ //If we have more than enough change, take as much as we need
				billsToGet = Math.floor(amountToGet/bill) as Integer
			} else { //Otherwise, take every bill in the register and move on to the next smaller bill
				billsToGet = cash[bill]
			}
			change.put(bill, billsToGet)
			amountToGet -= (bill * billsToGet)
		}
		
		if(calcTotal(change) == amount){ //If what's in the change map equals the original amount, you succeeded!
			return change
		} else {
			println "Sorry, I don't have change for \$$amount"
			return [:]
		}
	}
}

def main(){
	def register = new Register()

	def input = ""
	while((input = System.console().readLine("> ")) != "quit"){ //While user hasn't quit
		def (command, args) = [input.split().head(), input.split().tail()] //Get command and arguments		
		switch(command){ //Switch between every other command and quit
			case "show":
				println register
				break
			case "put":
			case "take":
				if(args.size() < register.cash.size()){
					println "You need to fill every slot in the register"
				} else {
					args = args.collect { it as Integer } //Cast all arguments to integers
					register."$command"(
						20: args[0],
						10: args[1],
						5: args[2],
						2: args[3],
						1: args[4],
					)
				}
				println register
				break
			case "change":
				if(args.size() > 1){
					println "You can only get change for one thing at a time"
				}
				def amount = args[0] as Integer
				def change = register.getChange(amount)
				register.take(change)
				println register
				break
		}
	}
	
	println "Bye"
	System.exit 0
}

main()