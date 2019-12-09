package com.fmi.pis.noise.diffusion;

import com.fmi.pis.noise.diffusion.filters.AnisotropicDiffusionFilter;
import com.fmi.pis.noise.diffusion.filters.AnisotropicDiffusionFilterVariant;
import com.fmi.pis.noise.diffusion.filters.Filter;
import com.fmi.pis.noise.diffusion.filters.IsotropicDiffusionFilter;

public class FilterFactory {

    public Filter getIsotropicDiffusionFilter() {
        return new IsotropicDiffusionFilter();
    }

    public Filter getAnisotropicDiffusionFilter() {
        return new AnisotropicDiffusionFilter();
    }

    public Filter getAnisotropicDiffusionFilterVariant(double kappa) {
        return new AnisotropicDiffusionFilterVariant(kappa);
    }
}
