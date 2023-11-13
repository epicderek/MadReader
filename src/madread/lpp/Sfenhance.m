function S = Sfenhance(alpha, v)
% 'alpha' is the EM coupling, 'v' the CM speed of the outgoing particle
% experiencing an EM Sommerfeld enhancement.

S = (2*pi*alpha./v) ./ (1 - exp(-2*pi*alpha./v));

