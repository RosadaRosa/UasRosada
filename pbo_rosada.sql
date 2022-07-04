-- phpMyAdmin SQL Dump
-- version 4.9.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 04 Jul 2022 pada 15.04
-- Versi server: 10.4.8-MariaDB
-- Versi PHP: 7.3.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `pbo_rosada`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `kucing`
--

CREATE TABLE `kucing` (
  `id` int(11) NOT NULL,
  `nama` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data untuk tabel `kucing`
--

INSERT INTO `kucing` (`id`, `nama`) VALUES
(1, 'Persia'),
(2, 'Anggora'),
(3, 'Bengal');

-- --------------------------------------------------------

--
-- Struktur dari tabel `spesies`
--

CREATE TABLE `spesies` (
  `id` int(11) NOT NULL,
  `nama` varchar(255) NOT NULL,
  `kucing_id` int(11) DEFAULT NULL,
  `klasifikasi` enum('TIPE A','TIPE B') DEFAULT NULL,
  `populasi` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data untuk tabel `spesies`
--

INSERT INTO `spesies` (`id`, `nama`, `kucing_id`, `klasifikasi`, `populasi`) VALUES
(1, 'Persia Peaknose', 1, 'TIPE A', 896544376),
(2, 'Bengal Rosetted', 3, 'TIPE B', 378256567),
(4, 'Persia Flatnose', 1, 'TIPE A', 900600543),
(5, 'Maine Coon', 2, 'TIPE A', 800540067),
(6, 'American Shortair', 2, 'TIPE A', 788764565);

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `kucing`
--
ALTER TABLE `kucing`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `spesies`
--
ALTER TABLE `spesies`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `kucing`
--
ALTER TABLE `kucing`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT untuk tabel `spesies`
--
ALTER TABLE `spesies`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
