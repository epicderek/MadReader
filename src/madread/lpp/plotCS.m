% % Madgraph dir
% data_dir = 'cases/e-e+_tau-tau+/trials/';
% Pythia dir
data_dir = 'C:/Users/derek/physics/hep-sim/pythia8310/examples/e-e+_ttbar/trials/';

% Set product mass, in GeV.
M = 173;

% Set EM coupling.
alpha = 1/127;

% Initial energy of one electron in CM frame, in units of GeV.
E = csvread(sprintf('%sbeam_energy_alpha.csv', data_dir), 1, 0);
% Infer velocity.
v = sqrt(1 - M^2./E.^2);

% Read in cross section data, in units of pb.
cs_data = csvread(sprintf('%scross_section_alpha.csv', data_dir), 1, 0);
cs = cs_data(:,1);
dcs = cs_data(:,2);

% Finer range of speed, for plotting; uniformly spaced or logarithmically.
nfine = 100;
vfine = v(1): (v(end)-v(1))/nfine: v(end);
vlfine = log(v(1)): (log(v(end))-log(v(1)))/nfine: log(v(end));
vlfine = exp(vlfine);
Efine = M*lfactor(vfine);
Elfine = M*lfactor(vlfine);

% Unenhanced cross sections. Possible adjustment of charge for quark
% products.
cs0 = lpphcsA(E, M, sqrt(3)*2/3, alpha);
cs0lfine = hcs(Elfine, M);
csElfine = cs0lfine.*Sfenhance(alpha, vlfine);

figure
errorbar(v, cs, dcs, '.', 'MarkerFaceColor', 'red', 'LineWidth', 1)
hold on
plot(vlfine, cs0lfine)
% Sommerfeld enhanced, using LO as hard cross section.
hold on
plot(vlfine, csElfine)
hold on
xline(alpha)

% Set to log-log scale.
set(gca, 'Xscale', 'log')
set(gca, 'Yscale', 'log')
xlabel('$v$')
ylabel('$\sigma$ (pb)')
title('(Pythia) cross sections for $e^-e^+\rightarrow t\ \bar{t}$ near $v=\alpha$')
legend({'simulated', 'QED LO', 'Sommerfeld-enhanced', '$\alpha = \frac{1}{127}$'})