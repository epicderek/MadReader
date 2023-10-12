data_dir = 'e-e+_a_tau-tau+/trials/';

% Set τ mass.
mtau = 100;

% Initial energy of one electron in CM frame, in units of GeV.
E = csvread(sprintf('%sbeam_energy4.csv', data_dir), 1, 0);
% Infer velocity.
v = sqrt(1 - mtau^2./E.^2);

% Read in cross section data, in units of pb.
cs_data = csvread(sprintf('%scross_section4.csv', data_dir), 1, 0);
cs = cs_data(:,1);
dcs = cs_data(:,2);

figure
scatter(v, cs ./ hcs(E, mtau), 'filled')
xlabel('$v$')
ylabel('$\sigma/\sigma_0$')
title('Sommerfeld enhancement for $\tau^-\tau^+$ production (4)')