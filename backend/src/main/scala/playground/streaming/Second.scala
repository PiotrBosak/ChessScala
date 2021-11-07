package playground.streaming

object Second {

  trait Event[A] {
    def data : A
  }

  case class Vehicle(name : String)
  case class VehicleEvent(data: Vehicle) extends Event[Vehicle]


}
