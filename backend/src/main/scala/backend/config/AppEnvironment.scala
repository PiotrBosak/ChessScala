package backend.config
import io.circe.Codec
import io.circe.*

enum AppEnvironment derives Codec.AsObject {
  case Test
  case Prod
}

