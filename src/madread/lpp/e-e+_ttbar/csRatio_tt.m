% Pythia dir
data_dir = 'C:/Users/derek/physics/hep-sim/pythia8310/examples/e-e+_ttbar/trials/';

% Set product mass.
M = 173;

% Initial energy of one electron in CM frame, in units of GeV.
E = csvread(sprintf('%sbeam_energy_alpha.csv', data_dir), 1, 0);
% Infer velocity.
v = sqrt(1 - M^2./E.^2);

% Read in cross section data, in units of pb. Scale by 10^9 for Pythia.
cs_data = csvread(sprintf('%scross_section_alpha.csv', data_dir), 1, 0);
cs = cs_data(:,1);
dcs = cs_data(:,2);

% Finer range of speed, for plotting; uniformly spaced or logarithmically.
nfine = 100;
vlfine = log(v(1)): (log(v(end))-log(v(1)))/nfine: log(v(end));
vlfine = exp(vlfine);

% Unenhanced cross sections.
cs0 = lpphcsA(E, M, sqrt(3)*2/3, alpha);

figure
% Ratio of cross sections.
csr = cs./cs0;
errorbar(v, csr, dcs./cs0, '.', 'MarkerFaceColor', 'red', 'LineWidth', 1)
hold on
plot(vlfine, Sfenhance(alpha, vlfine))
set(gca, 'Xscale', 'log')
ylim([0 max(csr)*1.1])
xlabel('$v$')
ylabel('$\sigma/\sigma_0$')
title('Enhancement factor for (Pythia) $e^-e^+\rightarrow t\ \bar{t}$')
legend({'simulated', 'EM Sommerfeld factor'})