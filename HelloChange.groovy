
class Register {

	Map<String, Integer> cash = [
		twenties: 0,
		tens: 0,
		fives: 0,
		twos: 0,
		ones: 0,
	]
	
	def put(Map<String, Integer> cashMap){
		cashMap.each { k, v -> 
			cash[k] += cashMap[k]
		}
	}
	
	def take(Map<String, Integer> cashMap){
		cashMap.each { k, v -> 
			cash[k] -= cashMap[k]
		}
	}
	
	Integer getTotal(){
		(cash.ones * 1) + (cash.twos * 2) + (cash.fives * 5) + (cash.tens * 10) + (cash.twenties * 20)
	}
	
	String toString(){
		"\$${getTotal()} ${cash.values().join(' ')}"
	}

}

def main(){
	def register = new Register()
	//register.put(ones: 2, twos: 1, fives: 1, tens: 1, twenties: 1)

	def input = ""
	while((input = System.console().readLine("> ")) != "quit"){ //While user hasn't quit
		def (command, args) = [input.split().head(), input.split().tail()] //Get command and arguments		
		switch(command){ //Switch between every other command and quit
			case "show":
				println register
				break
			case "put":
				if(args.size() < register.cash.keys().size()){
					println "You haven't specified the right amount to put in the register"
				} else {
					args = args.collect { it as Integer } //Cast all arguments to integers
					register.put(
						twenties: args[0],
						tens: args[1],
						fives: args[2],
						twos: args[3],
						ones: args[4],
					)
				}
				println register //Show end state of the register
				break
			case "take":
				if(args.size() < register.cash.keys().size()){
					println "You haven't specified the right amount to take from the register"
				} else {
					args = args.collect { it as Integer } //Cast all arguments to integers
					register.take(
						twenties: args[0],
						tens: args[1],
						fives: args[2],
						twos: args[3],
						ones: args[4],
					)
				}
				println register
				break
		}
	}
	
	println "Bye"
	System.exit 0
}

main()