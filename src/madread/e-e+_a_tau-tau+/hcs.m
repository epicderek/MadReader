% Hard cross section without Sommerfeld enhancement, in unit of pb.
function s = hcs(E, M)
    % Constant.
    a = 1/137;
    
    Esq = E.^2;
    s = pi*a^2./(4*Esq).*sqrt(1 - M^2./Esq).*(4/3 + 2/3*M^2./Esq);
    % Convert from GeV^-2 to pb.
    s = s / 5.07^2*10^10;
end