package com.fmi.pis.noise.diffusion.filters;


public class AnisotropicDiffusionFilterVariant extends AnisotropicDiffusionFilter {
    /// members
    private double kappa = 5;

    /// methods

    /**
     * @param intensity
     * @return
     * @brief
     */
    @Override
    protected double flux_derivative(double intensity) {
        return 1 / (1 + Math.pow(intensity / this.kappa, 2));
    }

    /// ctors

    /**
     * @param kappa
     * @brief
     */
    public AnisotropicDiffusionFilterVariant(double kappa) {
        this.kappa = kappa;
    }
}
