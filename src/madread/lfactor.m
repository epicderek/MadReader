function g = lfactor(v)
% The Lorentz factor as a function of the fractional speed of the particle
% to the speed of light.

g = 1 ./ sqrt(1-v.^2);