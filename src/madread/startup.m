% Add that folder plus all subfolders to the path.
global rootFolder
rootFolder = fileparts(which(mfilename)); 
% Add that folder plus all subfolders to the path.
addpath(genpath(rootFolder));
% Set default font for plotting as latex.
set(0, 'defaultTextInterpreter', 'latex')
set(0, 'DefaultLegendInterpreter', 'latex')
