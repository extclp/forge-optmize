package forge.optimize.dummy;

import cpw.mods.modlauncher.api.ITransformer;
import net.minecraftforge.forgespi.coremod.ICoreModFile;
import net.minecraftforge.forgespi.coremod.ICoreModProvider;

import java.util.List;

public class DummyCoreModProvider implements ICoreModProvider {

    @Override
    public void addCoreMod(ICoreModFile file) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<ITransformer<?>> getCoreModTransformers() {
        return List.of();
    }
}
