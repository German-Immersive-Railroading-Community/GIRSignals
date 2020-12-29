package net.gir.girsignals.controllers;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.SimpleComponent;
import net.gir.girsignals.items.Linkingtool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "opencomputers")
public class SignalControllerTileEntity extends TileEntity implements SimpleComponent {

	private BlockPos linkedSignalPosition = null;
	private final SignalType signalType;

	public SignalControllerTileEntity() {
		this.signalType = null;
	}

	public SignalControllerTileEntity(SignalType signalType) {
		this.signalType = signalType;
	}

	public static BlockPos readBlockPosFromNBT(NBTTagCompound compound) {
		if (compound.hasKey("x") && compound.hasKey("y") && compound.hasKey("z")) {
			BlockPos pos = new BlockPos(compound.getInteger("x"), compound.getInteger("y"), compound.getInteger("z"));
			return pos;
		}
		return null;
	}

	public static void writeBlockPosToNBT(BlockPos pos, NBTTagCompound compound) {
		if (pos != null) {
			compound.setInteger("x", pos.getX());
			compound.setInteger("y", pos.getY());
			compound.setInteger("z", pos.getZ());
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		linkedSignalPosition = readBlockPosFromNBT(compound);
		super.readFromNBT(compound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		writeBlockPosToNBT(linkedSignalPosition, compound);
		return super.writeToNBT(compound);
	}

	public boolean link(ItemStack stack) {
		if (stack.getItem() instanceof Linkingtool) {
			return (linkedSignalPosition = readBlockPosFromNBT(stack.getTagCompound())) != null;
		}
		return false;
	}

	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] hasLink(Context context, Arguments args) {
		return new Object[] { hasLinkImpl() };
	}

	public boolean hasLinkImpl() {
		return linkedSignalPosition != null;
	}

	public void unlink() {
		linkedSignalPosition = null;
	}

	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getSupportedSignalStates(Context context, Arguments args) {
		int[] in = getSupportedSignalStatesImpl();
		Integer[] newin = new Integer[in.length];
		for (int i = 0; i < newin.length; i++) {
			newin[i] = in[i];
		}
		return newin;
	}

	public int[] getSupportedSignalStatesImpl() {
		if (!hasLinkImpl())
			return new int[] {};
		return signalType.supportedSignalStates.getSupportedSignalStates(world, linkedSignalPosition,
				world.getBlockState(linkedSignalPosition));
	}

	public static boolean find(int[] arr, int i) {
		for (int x : arr)
			if (x == i)
				return true;
		return false;
	}

	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] changeSignal(Context context, Arguments args) {
		return new Object[] { changeSignalImpl(args.checkInteger(0)) };
	}

	public boolean changeSignalImpl(int newSignal) {
		if (!hasLinkImpl() || !find(getSupportedSignalStatesImpl(), newSignal))
			return false;
		IBlockState oldState = world.getBlockState(linkedSignalPosition);
		IBlockState state = signalType.onSignalChange.getNewState(world, linkedSignalPosition, oldState, newSignal);
		if (oldState == state)
			return false;
		return world.setBlockState(linkedSignalPosition, state);
	}

	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getSignalType(Context context, Arguments args) {
		return new Object[] { signalType.name };
	}

	public String getSignalTypeImpl() {
		return signalType.name;
	}

	@Override
	public String getComponentName() {
		return "signaltile";
	}

}