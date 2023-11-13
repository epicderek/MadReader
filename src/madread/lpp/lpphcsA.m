% LO cross section for lepton pair production (e+ e- --> f fbar), e.g. τ+,
% τ- as products, or t, tbar. The needed parameters are the initial energy
% of e- in CM frame, the mass of the product (f), and the electric charge
% of the product, in units of electron charge.
function s = lpphcsA(E, M, q, alpha)
    % Coupling constant.
    if ~exist('alpha', 'var')
        alpha = 1/127;
    end
    % Electron mass.
    m = 0.511*10^(-3);
    
    Esq = E.^2;
    % s = pi*a^2./(4*Esq).*sqrt(1 - M^2./Esq).*(4/3 + 2/3*M^2./Esq);
    % Without ultrarelativistic approximation for e+, e-.
    s = pi*alpha^2./(12*Esq.^2).*sqrt(Esq-M.^2)./sqrt(Esq-m^2).*(2*(2*Esq + m^2 + M.^2) + m^2*M.^2./Esq);
    % Convert from GeV^-2 to pb. Uncertainty in 10^(-3). Fix!
    s = s / 5.07^2*10^10;
    % Scale by product's charge.
    s = s*q^2;
end