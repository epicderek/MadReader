data_dir = 'e-e+_a_tau-tau+/trials/';

% Set τ mass.
mtau = 100;

% Initial energy of one electron in CM frame, in units of GeV.
E = csvread(sprintf('%sbeam_energy3.csv', data_dir), 1, 0);
% Infer velocity.
v = sqrt(1 - mtau^2./E.^2);

% Read in cross section data, in units of pb.
cs_data = csvread(sprintf('%scross_section_3alt.csv', data_dir), 1, 0);
cs = cs_data(:,1);
dcs = cs_data(:,2);

% Finer range of speed, for plotting; uniformly spaced or logarithmically.
nfine = 100;
vfine = v(1): (v(end)-v(1))/nfine: v(end);
vlfine = log(v(1)): (log(v(end))-log(v(1)))/nfine: log(v(end));
vlfine = exp(vlfine);
Efine = mtau*lfactor(vfine);
Elfine = mtau*lfactor(vlfine);

% Unenhanced cross sections.
cs0 = hcs(E, mtau);

figure
tiledlayout('vertical')

% Cross section plot.
% figure
nexttile
errorbar(v, cs, dcs, '.', 'MarkerFaceColor', 'red', 'LineWidth', 1)
hold on
plot(vlfine, hcs(Elfine, mtau))
set(gca, 'Xscale', 'log')
xlabel('$v$')
ylabel('$\sigma$ (pb)')
% ylim([6*10^(-3) 10.5*10^(-3)])
title('cross sections for $e^-e^+\rightarrow\gamma\rightarrow\tau^-\tau^+$ production (2)')
legend({'simulated', 'hard'})

% figure
nexttile
% Ratio of cross sections.
csr = cs./cs0;
errorbar(v, csr, dcs./cs0, '.', 'MarkerFaceColor', 'red', 'LineWidth', 1)
set(gca, 'Xscale', 'log')
ylim([0 max(csr)*1.1])
xlabel('$v$')
ylabel('$\sigma/\sigma_0$')
title('Sommerfeld enhancement for $e^-e^+\rightarrow\gamma\rightarrow\tau^-\tau^+$ production (2)')