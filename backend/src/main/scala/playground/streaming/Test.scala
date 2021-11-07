package playground.streaming

object Test {

  abstract class Employee
  class Worker extends Employee
  class Manager(emps : List[Employee]) extends Worker {
    val employees : List[Employee] = emps

    def findAllSubordinates:List[Employee] = emps.foldLeft(List.empty[Employee]) { case (z,emp) =>
      emp match {
        case man : Manager => z ++ man.findAllSubordinates
        case e => z :+ e
      }
    }

  }

}
