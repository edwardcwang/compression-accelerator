package example

import chisel3._
import freechips.rocketchip.tile.OpcodeSet
import freechips.rocketchip.config._
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.system._
import freechips.rocketchip.subsystem._
import freechips.rocketchip.tile._
import freechips.rocketchip.rocket._

// Difficult to create these manually
/*
class HasTileKeyConfig extends Config((site, here, up) => {
  case TileKey => RocketTileParams(???)
  //case SharedMemoryTLEdge => ???
})
*/

/**
 * Get parameters for the accelerator from the full system.
 */
object AcceleratorParams {
  def apply() = {
    //val p: Parameters = (new HasTileKeyConfig ++ new DefaultExampleConfig)

    // Elaborate the full system first to get the tile parameters needed for RoCC
    // e.g. SharedMemoryTLEdge and TileKey
    val accelP: Parameters = (new CompressionAcceleratorConfig)
    val tileParams = LazyModule(new ExampleRocketSystem()(accelP)).rocketTiles(0).p

    // These parameters can be used to generate a compression accelerator
    tileParams
  }
}

object TestGenerate extends App {
  val dutGen = () => LazyModule(new CompressionAccelerator(OpcodeSet.custom3)(AcceleratorParams())).module
  chisel3.Driver.execute(Array[String](), dutGen)
}
