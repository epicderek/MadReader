% Total momentum is 0. Given the identical masses of the antiparticles in
% this 2-2 scattering, the kinematics of one determines the other.

expdir = 'pp_a_tau-tau+/run_06/';
dir = strcat(expdir, 'sim_momenta/');

multiplicity = 4;

% Array of the 4-momentum matrices (over events) for participant particles.
p_4 = cell(1, multiplicity);

for k = 1: multiplicity
    % Skip header.
    p_4{k} = csvread(sprintf('%ssim_momenta_%d.csv', dir, k), 1, 0);
end

% Split to 3-momenta and energies for ease of computation.
E = cell(1, multiplicity);
p = cell(1, multiplicity);
for k = 1: multiplicity
    E{k} = p_4{k}(:,1);
    p{k} = p_4{k}(:,2:4);
end

% Record number of simulated events.
nevents = length(E{1});

v3 = zeros(nevents, 1);
v4 = zeros(nevents, 1);
% Compute velocity from momentum.
for j = 1: 3
    v3(:,j) = p{3}(:,j) ./ E{3};
    v4(:,j) = p{4}(:,j) ./ E{4};
end
% Final relative speed between the product(s).
v = sqrt(sum((v4-v3).^2, 2));

% Note velocity constraint from p conservation.

figure
histogram(v, 400)
xlabel('relative speed $v$')
ylabel('frequency')
title('$m_\tau = 10$ TeV, $E_i = (10.01, 10.01)$ TeV')

% figure;
% scatter(v3, v4, 'filled')

