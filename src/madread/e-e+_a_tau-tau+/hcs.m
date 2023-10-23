% Hard cross section (without Sommerfeld enhancement), in unit of pb.
function s = hcs(E, M)
    % Coupling constant.
    a = 1/137;
    % Electron mass.
    m = 0.511*10^(-3);
    
    Esq = E.^2;
    % s = pi*a^2./(4*Esq).*sqrt(1 - M^2./Esq).*(4/3 + 2/3*M^2./Esq);
    % Without ultrarelativistic approximation for e+, e-.
    s = pi*a^2./(12*Esq.^2).*sqrt(Esq-M^2)./sqrt(Esq-m^2).*(2*(2*Esq + m^2 + M^2) + m^2*M^2./Esq);
    % Convert from GeV^-2 to pb.
    s = s / 5.07^2*10^10;
end